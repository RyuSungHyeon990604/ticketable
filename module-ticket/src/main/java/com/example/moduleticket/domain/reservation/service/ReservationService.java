package com.example.moduleticket.domain.reservation.service;


import static com.example.moduleticket.global.exception.ErrorCode.INVALID_RESERVATION_STATE;
import static com.example.moduleticket.global.exception.ErrorCode.RESERVATION_NOT_FOUND;

import com.example.moduleticket.domain.reservation.dto.ReservationCreateRequest;
import com.example.moduleticket.domain.reservation.dto.ReservationResponse;
import com.example.moduleticket.domain.reservation.entity.Reservation;
import com.example.moduleticket.domain.reservation.entity.ReserveSeat;
import com.example.moduleticket.domain.reservation.event.TicketEvent;
import com.example.moduleticket.domain.reservation.event.publisher.TicketPublisher;
import com.example.moduleticket.domain.reservation.repository.ReservationRepository;
import com.example.moduleticket.domain.ticket.dto.response.TicketResponse;
import com.example.moduleticket.domain.ticket.event.SeatHoldReleaseEvent;
import com.example.moduleticket.domain.ticket.service.TicketService;
import com.example.moduleticket.global.argumentresolver.AuthUser;
import com.example.moduleticket.global.exception.ServerException;
import com.example.moduleticket.util.SeatHoldRedisUtil;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReservationService {

	private final ReservationRepository reservationRepository;
	private final ReservationCreateService reservationCreateService;
	private final ReservationPaymentService reservationPaymentService;
	private final ReservationValidator reservationValidator;
	private final SeatHoldRedisUtil seatHoldRedisUtil;
	private final TicketService ticketService;
	private final ApplicationEventPublisher eventPublisher;
	private final TicketPublisher ticketPublisher;

	public ReservationResponse processReserve(AuthUser auth, ReservationCreateRequest reservationCreateRequest) {
		seatHoldRedisUtil.holdSeatAtomic(
			reservationCreateRequest.getGameId(),
			reservationCreateRequest.getSeatIds(),
			String.valueOf(auth.getMemberId())
		);
		try {
			ReservationResponse reservation = reservationCreateService.createReservation(
				auth,
				reservationCreateRequest
			);
			// 캐시 이벤트 발생
			TicketEvent ticketEvent = new TicketEvent(
				reservationCreateRequest.getGameId(),
				reservationCreateRequest.getSeatIds().get(0)
			);
			ticketPublisher.publish(ticketEvent);

			return reservation;
		} catch (Exception e) {
			seatHoldRedisUtil.releaseSeatAtomic(reservationCreateRequest.getSeatIds(),reservationCreateRequest.getGameId());
			throw e;
		}
	}

	@Transactional
	public TicketResponse processReservationCompletion(AuthUser authUser, Long reservationId) {
		Reservation reservation = reservationRepository.findByIdMemberId(reservationId, authUser.getMemberId())
			.orElseThrow(() -> new ServerException(RESERVATION_NOT_FOUND));

		if (reservation.getState().equals("COMPLETE_PAYMENT")) {
			log.info("이미 결제된 예약입니다.");
			return ticketService.getTicketByReservationId(reservationId);
		}

		List<Long> seatIds = reservation.getReserveSeats().stream().map(ReserveSeat::getSeatId).toList();
		Long gameId = reservation.getGameId();

		//authUser가 선점한 좌석이맞는지 최종 확인 & TTL만료시 에약완료 불가
		reservationValidator.validateReservationForComplete(authUser, gameId, seatIds, reservation);

		reservationPaymentService.reservePayment(authUser, reservationId, reservation.getTotalPrice());

		//티켓 생성
		TicketResponse ticketResponse = ticketService.issueTicketFromReservation(
			authUser,
			gameId,
			seatIds,
			reservation
		);

		reservation.completePayment();
		eventPublisher.publishEvent(new SeatHoldReleaseEvent(seatIds, gameId));

		return ticketResponse;
	}


	@Transactional
	public void cancelReservation(AuthUser authUser, Long reservationId) {
		Reservation reservation = reservationRepository.findByIdMemberId(reservationId, authUser.getMemberId())
			.orElseThrow(() -> new ServerException(RESERVATION_NOT_FOUND));
		if(reservation.getState().equals("WAITING_PAYMENT")) {
			List<Long> seatIds = reservation.getReserveSeats().stream().map(ReserveSeat::getSeatId).toList();
			reservation.cancelPayment();
			eventPublisher.publishEvent(new SeatHoldReleaseEvent(seatIds, authUser.getMemberId()));
			return;
		}

		throw new ServerException(INVALID_RESERVATION_STATE);
	}


	@Scheduled(cron = "0 * * * * *")
	@Transactional
	public void proceedReservationExpire() {
		LocalDateTime expiredLimit = LocalDateTime.now().minusMinutes(15);
		int updated = reservationRepository.updateExpiredReservations(expiredLimit);
		log.info("{} rows updated", updated);
	}
	public Set<Long> getBookedSeatsId(Long gameId) {
		return reservationRepository.findBookedSeatIdByGameId(gameId);
	}
}
