package com.example.moduleticket.domain.reservation.service;

import static com.example.modulecommon.exception.ErrorCode.RESERVATION_NOT_FOUND;
import static com.example.modulecommon.exception.ErrorCode.SEAT_HOLD_EXPIRED;
import static com.example.modulecommon.exception.ErrorCode.SEAT_NOT_FOUND;

import com.example.modulecommon.entity.AuthUser;
import com.example.modulecommon.exception.ServerException;
import com.example.moduleticket.domain.reservation.dto.ReservationCreateRequest;
import com.example.moduleticket.domain.reservation.dto.ReservationDto;
import com.example.moduleticket.domain.reservation.dto.ReservationResponse;
import com.example.moduleticket.domain.reservation.entity.Reservation;
import com.example.moduleticket.domain.reservation.entity.ReserveSeat;
import com.example.moduleticket.domain.reservation.repository.ReservationRepository;
import com.example.moduleticket.domain.ticket.service.TicketSeatService;
import com.example.moduleticket.feign.GameClient;
import com.example.moduleticket.feign.SeatClient;
import com.example.moduleticket.feign.dto.GameDto;
import com.example.moduleticket.feign.dto.SeatDto;
import com.example.moduleticket.domain.ticket.dto.response.TicketResponse;
import com.example.moduleticket.domain.ticket.event.SeatHoldReleaseEvent;
import com.example.moduleticket.domain.ticket.service.TicketService;
import com.example.moduleticket.util.SeatHoldRedisUtil;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
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

	@Transactional
	public ReservationResponse createReservation(AuthUser auth, ReservationCreateRequest reservationCreateRequest) {

		//이미 예약된 좌석이 존재하는지 체크
		ticketSeatService.checkDuplicateSeats(reservationCreateRequest.getSeatIds(),
			reservationCreateRequest.getGameId());
		//검증이 완료되면 좌석 점유
		seatHoldRedisUtil.holdSeatAtomic(reservationCreateRequest.getGameId(), reservationCreateRequest.getSeatIds(),
			String.valueOf(auth.getMemberId()));

		GameDto gameDto = null;
		List<SeatDto> seats = new ArrayList<>();
		try {
			gameDto = gameClient.getGame(reservationCreateRequest.getGameId());
			seats = seatClient.getSeats(reservationCreateRequest.getGameId(),
				reservationCreateRequest.getSeatIds());
		} catch (Exception e) {
			//경기, 좌석 정보를 가져오는도중 예외발생 시 선점했던 좌석을 해제
			seatHoldRedisUtil.releaseSeatAtomic(reservationCreateRequest.getSeatIds(),
				reservationCreateRequest.getGameId());
			throw new ServerException(SEAT_NOT_FOUND);
		}

		int seatPrice = seats.stream().mapToInt(SeatDto::getPrice).sum();

		//예약 데이터 생성
		Reservation reservation = new Reservation(auth.getMemberId(), gameDto.getId(), "WAITING_PAYMENT", seatPrice);
		List<ReserveSeat> ReserveSeatList = ReserveSeat.from(seats);
		for (ReserveSeat reserveSeat : ReserveSeatList) {
			reservation.addSeat(reserveSeat);
		}
		reservationRepository.save(reservation);

		return ReservationResponse.from(reservation, gameDto, seats);
	}

	@Transactional
	public TicketResponse completePaymentReservation(AuthUser authUser, Long reservationId) {
		Reservation reservation = reservationRepository.findByIdAndStateAndMemberId(reservationId,
				authUser.getMemberId(), "WAITING_PAYMENT")
			.orElseThrow(() -> new ServerException(RESERVATION_NOT_FOUND));
		List<Long> seatIds = reservation.getReservations().stream().map(ReserveSeat::getSeatId).toList();
		Long gameId = reservation.getGameId();

		//authUser가 선점한 좌석이맞는지 최종 확인 & TTL만료시 에약완료 불가
		try {
			seatHoldRedisUtil.checkHeldSeatAtomic(seatIds, gameId, String.valueOf(authUser.getMemberId()));
		} catch (ServerException e) {
			// 선점한 좌석이 만료되었을경우 예약상태를 만료상태로 변경
			if(e.getErrorCode() == SEAT_HOLD_EXPIRED){
				log.info("TTL 만료로 예약 만료 처리됨: reservationId = {}", reservationId);
				reservation.expiredPayment();
			}
			throw e;
		}

		ReservationDto reservationDto = ReservationDto.from(reservation);

		//티켓 정보를 생성
		TicketResponse ticketResponse = ticketService.issueTicketFromReservation(authUser, reservationDto);

		reservation.completePayment();
		eventPublisher.publishEvent(new SeatHoldReleaseEvent(seatIds, gameId));

		return ticketResponse;
	}

}
