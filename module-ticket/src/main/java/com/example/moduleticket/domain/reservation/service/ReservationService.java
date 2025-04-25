package com.example.moduleticket.domain.reservation.service;

import static com.example.modulecommon.exception.ErrorCode.INVALID_RESERVATION_STATE;
import static com.example.modulecommon.exception.ErrorCode.RESERVATION_NOT_FOUND;
import static com.example.modulecommon.exception.ErrorCode.SEAT_HOLD_EXPIRED;
import static com.example.modulecommon.exception.ErrorCode.SEAT_NOT_FOUND;
import static com.example.modulecommon.exception.ErrorCode.TICKET_ALREADY_RESERVED;

import com.example.modulecommon.entity.AuthUser;
import com.example.modulecommon.exception.ServerException;
import com.example.moduleticket.domain.reservation.dto.ReservationCreateRequest;
import com.example.moduleticket.domain.reservation.dto.ReservationResponse;
import com.example.moduleticket.domain.reservation.entity.Reservation;
import com.example.moduleticket.domain.reservation.entity.ReserveSeat;
import com.example.moduleticket.domain.reservation.repository.ReservationRepository;
import com.example.moduleticket.domain.ticket.event.ReservationUnknownFailureEvent;
import com.example.moduleticket.domain.ticket.service.TicketSeatService;
import com.example.moduleticket.feign.GameClient;
import com.example.moduleticket.feign.PaymentClient;
import com.example.moduleticket.feign.SeatClient;
import com.example.moduleticket.feign.dto.GameDto;
import com.example.moduleticket.feign.dto.SeatDto;
import com.example.moduleticket.domain.ticket.dto.response.TicketResponse;
import com.example.moduleticket.domain.ticket.event.SeatHoldReleaseEvent;
import com.example.moduleticket.domain.ticket.service.TicketService;
import com.example.moduleticket.feign.dto.request.PaymentRequest;
import com.example.moduleticket.util.IdempotencyKeyUtil;
import com.example.moduleticket.util.SeatHoldRedisUtil;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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
	private final SeatHoldRedisUtil seatHoldRedisUtil;
	private final TicketService ticketService;
	private final TicketSeatService ticketSeatService;
	private final GameClient gameClient;
	private final SeatClient seatClient;
	private final ApplicationEventPublisher eventPublisher;
	private final PaymentClient paymentClient;

	@Transactional
	public ReservationResponse createReservation(AuthUser auth, ReservationCreateRequest reservationCreateRequest) {

		//이미 예약된 좌석이 존재하는지 체크
		ticketSeatService.checkDuplicateSeats(reservationCreateRequest.getSeatIds(),
			reservationCreateRequest.getGameId());

		if(reservationRepository.existsByReserveSeats_SeatIdInAndState(
			reservationCreateRequest.getSeatIds(),
			"WAITING_PAYMENT")
		) {
			throw new ServerException(TICKET_ALREADY_RESERVED);
		}

		//검증이 완료되면 좌석 점유
		seatHoldRedisUtil.holdSeatAtomic(
			reservationCreateRequest.getGameId(),
			reservationCreateRequest.getSeatIds(),
			String.valueOf(auth.getMemberId())
		);

		GameDto gameDto = null;
		List<SeatDto> seats = new ArrayList<>();
		try {
			gameDto = gameClient.getGame(reservationCreateRequest.getGameId());
			seats = seatClient.getSeatsByGameAndSection(
				reservationCreateRequest.getGameId(),
				reservationCreateRequest.getSectionId(),
				reservationCreateRequest.getSeatIds()
			);
		} catch (Exception e) {
			//경기, 좌석 정보를 가져오는도중 예외발생 시 선점했던 좌석을 해제
			seatHoldRedisUtil.releaseSeatAtomic(reservationCreateRequest.getSeatIds(),
				reservationCreateRequest.getGameId());
			throw new ServerException(SEAT_NOT_FOUND);
		}

		int seatPrice = seats.stream().mapToInt(SeatDto::getPrice).sum();

		//예약 데이터 생성
		Reservation reservation = new Reservation(
			auth.getMemberId(),
			gameDto.getId(),
			"WAITING_PAYMENT",
			seatPrice
		);
		List<ReserveSeat> ReserveSeatList = ReserveSeat.from(reservationCreateRequest.getSectionId(), seats);
		for (ReserveSeat reserveSeat : ReserveSeatList) {
			reservation.addSeat(reserveSeat);
		}
		reservationRepository.save(reservation);

		return ReservationResponse.from(reservation, gameDto, seats);
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
		try {
			ticketSeatService.checkDuplicateSeats(seatIds, gameId);
			seatHoldRedisUtil.checkHeldSeatAtomic(seatIds, gameId, String.valueOf(authUser.getMemberId()));
		} catch (ServerException e) {
			// 선점한 좌석이 만료되었을경우 예약상태를 만료상태로 변경
			if (e.getErrorCode() == SEAT_HOLD_EXPIRED) {
				log.info("TTL 만료로 예약 만료 처리됨: reservationId = {}", reservationId);
				reservation.expiredPayment();
			}
			throw e;
		}

		PaymentRequest paymentRequest = new PaymentRequest(
			reservation.getTotalPrice(),
			"decrement",
			"reservation",
			reservationId
		);

		//결제요청
		try {
			paymentClient.processPayment(
				IdempotencyKeyUtil.forReservation(reservationId, "decrement"),
				authUser.getMemberId(),
				paymentRequest
			);
		} catch (ServerException e) {
			throw e;
		} catch (Exception e) {
			//알수없는 오류이므로 다시결제요청
			reTryPayment(
				paymentRequest,
				IdempotencyKeyUtil.forReservation(reservationId, "decrement"),
				authUser.getMemberId(),
				reservation
			);
		}

		GameDto gameDto = gameClient.getGame(gameId);
		List<SeatDto> seatDtos = seatClient.getSeatsByGame(gameId, seatIds);

		//티켓 생성
		TicketResponse ticketResponse = ticketService.issueTicketFromReservation(
			authUser,
			gameDto,
			seatDtos,
			reservation
		);

		reservation.completePayment();
		eventPublisher.publishEvent(new SeatHoldReleaseEvent(seatIds, gameId));

		return ticketResponse;
	}

	private void reTryPayment(PaymentRequest request, String idempotencyKey, Long memberId, Reservation reservation) {
		try {
			paymentClient.processPayment(idempotencyKey, memberId, request);
		} catch (ServerException e) {
			throw e;
		} catch (Exception e) {
			//알수없는 예외로 마킹
			eventPublisher.publishEvent(new ReservationUnknownFailureEvent(reservation.getId()));
			throw e;
		}
	}

	@Transactional
	public void cancelReservation(AuthUser authUser, Long reservationId) {
		Reservation reservation = reservationRepository.findByIdMemberId(reservationId, authUser.getMemberId())
			.orElseThrow(() -> new ServerException(RESERVATION_NOT_FOUND));
		if(reservation.getState().equals("WAITING_PAYMENT")) {
			reservation.cancelPayment();
			return;
		}
		throw new ServerException(INVALID_RESERVATION_STATE);
	}


	@Scheduled(cron = "0 * * * * *")
	@Transactional
	public void proceedReservationExpire() {
		LocalDateTime expiredLimit = LocalDateTime.now().minusMinutes(15);
		reservationRepository.updateExpiredReservations(expiredLimit);
	}
}
