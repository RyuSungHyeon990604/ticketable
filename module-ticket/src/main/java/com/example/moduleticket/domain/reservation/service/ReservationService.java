package com.example.moduleticket.domain.reservation.service;


import static com.example.moduleticket.global.exception.ErrorCode.INVALID_RESERVATION_STATE;
import static com.example.moduleticket.global.exception.ErrorCode.RESERVATION_NOT_FOUND;

import com.example.moduleticket.domain.reservation.context.SeatHoldContext;
import com.example.moduleticket.domain.reservation.dto.ReservationCreateRequest;
import com.example.moduleticket.domain.reservation.entity.Reservation;
import com.example.moduleticket.domain.reservation.entity.ReserveSeat;
import com.example.moduleticket.domain.reservation.event.ReservationCancelledEvent;
import com.example.moduleticket.domain.reservation.event.ReservationExpiredEvent;
import com.example.moduleticket.domain.reservation.event.ReservationPaymentComplete;
import com.example.moduleticket.domain.reservation.event.publisher.ReservationEventPublisher;
import com.example.moduleticket.domain.reservation.repository.ReservationRepository;
import com.example.moduleticket.global.argumentresolver.AuthUser;
import com.example.moduleticket.global.dto.ApiResponse;
import com.example.moduleticket.global.exception.ServerException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
	private final SeatHoldService seatHoldService;
	private final ReservationEventPublisher reservationEventPublisher;

	public ApiResponse<Void> createReservationWithHold(AuthUser auth, ReservationCreateRequest reservationCreateRequest) {
		Long gameId = reservationCreateRequest.getGameId();
		List<Long> seatIds = reservationCreateRequest.getSeatIds();
		Long memberId = auth.getMemberId();
		try (SeatHoldContext hold = seatHoldService.hold(memberId, gameId,seatIds)) {
			reservationCreateService.createReservation(
				auth,
				reservationCreateRequest
			);
			hold.markReservationSuccess();
			return ApiResponse.messageOnly("예약이 완료되었습니다. 15분내로 결제를 완료해주세요");
		}
	}

	@Transactional
	public ApiResponse<Void> completeReservationPayment(AuthUser authUser, Long reservationId) {
		Reservation reservation = reservationRepository.findByIdMemberId(reservationId, authUser.getMemberId())
			.orElseThrow(() -> new ServerException(RESERVATION_NOT_FOUND));

		if (reservation.isCompletePayment()) {
			log.info("이미 결제된 예약입니다.");
			return ApiResponse.messageOnly("이미 결제된 예약입니다.");
		}

		if(!reservation.isPayable()) {
			return ApiResponse.messageOnly("결제를 진행 할 수 없는 예약내역입니다.");
		}
		List<Long> seatIds = reservation.getReserveSeats().stream().map(ReserveSeat::getSeatId).toList();
		Long gameId = reservation.getGameId();

		//authUser가 선점한 좌석이맞는지 최종 확인 & TTL만료시 에약완료 불가
		reservationValidator.validateReservationForComplete(authUser, gameId, seatIds, reservation);

		reservationPaymentService.reservePayment(authUser, reservationId, reservation.getTotalPrice(), seatIds, gameId);
		reservation.completePayment();

		ReservationPaymentComplete reservationPaymentComplete = new ReservationPaymentComplete(
			reservation.getId(),
			authUser.getMemberId(),
			gameId,
			seatIds
		);
		reservationEventPublisher.handleReservationCompleted(reservationPaymentComplete);

		return ApiResponse.messageOnly("티켓 결제가 완료되었습니다");
	}


	@Transactional
	public void cancelReservation(AuthUser authUser, Long reservationId) {
		Reservation reservation = reservationRepository.findByIdMemberId(reservationId, authUser.getMemberId())
			.orElseThrow(() -> new ServerException(RESERVATION_NOT_FOUND));
		if(reservation.isCancelable()) {
			List<Long> seatIds = reservation.getReserveSeats().stream().map(ReserveSeat::getSeatId).toList();
			reservation.cancelPayment();

			ReservationCancelledEvent reservationCancelledEvent = new ReservationCancelledEvent(
				reservation.getId(),
				authUser.getMemberId(),
				reservation.getGameId(),
				seatIds
			);
			reservationEventPublisher.handleReservationCancelled(reservationCancelledEvent);
			return;
		}

		throw new ServerException(INVALID_RESERVATION_STATE);
	}


	@Scheduled(cron = "0 * * * * *")
	@Transactional
	public void proceedReservationExpire() {
		LocalDateTime expiredLimit = LocalDateTime.now().minusMinutes(15);
		List<Reservation> expiredReservationList = reservationRepository.findExpiredReservations(expiredLimit);
		log.info("{} rows updated", expiredReservationList.size());

		for (Reservation reservation : expiredReservationList) {
			reservation.expiredPayment();
			List<Long> seatIds = reservation.getReserveSeats().stream().map(ReserveSeat::getSeatId).toList();

			ReservationExpiredEvent reservationExpiredEvent = new ReservationExpiredEvent(
				reservation.getId(),
				reservation.getMemberId(),
				reservation.getGameId(),
				seatIds
			);
			reservationEventPublisher.handleReservationExpired(reservationExpiredEvent);
		}
	}

	public Set<Long> getBookedSeatsId(Long gameId) {
		return reservationRepository.findBookedSeatIdByGameId(gameId);
	}
}
