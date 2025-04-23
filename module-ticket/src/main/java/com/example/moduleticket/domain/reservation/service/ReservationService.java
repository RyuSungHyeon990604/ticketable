package com.example.moduleticket.domain.reservation.service;

import com.example.modulecommon.entity.AuthUser;
import com.example.moduleticket.domain.reservation.dto.ReservationCreateRequest;
import com.example.moduleticket.domain.reservation.dto.ReservationDto;
import com.example.moduleticket.domain.reservation.dto.ReservationResponse;
import com.example.moduleticket.domain.reservation.entity.Reservation;
import com.example.moduleticket.domain.reservation.entity.ReserveSeat;
import com.example.moduleticket.domain.reservation.repository.ReservationRepository;
import com.example.moduleticket.domain.ticket.dto.GameDto;
import com.example.moduleticket.domain.ticket.dto.SeatDto;
import com.example.moduleticket.domain.ticket.dto.response.TicketResponse;
import com.example.moduleticket.domain.ticket.event.SeatHoldReleaseEvent;
import com.example.moduleticket.domain.ticket.service.TicketService;
import com.example.moduleticket.util.SeatHoldRedisUtil;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReservationService {
	private final ReservationRepository reservationRepository;
	private final SeatHoldRedisUtil seatHoldRedisUtil;
	private TicketService ticketService;
	private final ApplicationEventPublisher eventPublisher;

	//private final GameService gameService;
	//private final SeatService seatService;
	//private final SeatPriceCalculator seatPriceCalculator;
	//private final SeatValidator seatValidator;

	@Transactional
	public ReservationResponse createReservation(AuthUser auth , ReservationCreateRequest reservationCreateRequest) {

		//todo : 검증 api 호출
//		seatValidator.checkHeldSeatFromRedis(auth, reservationCreateRequest.getGameId(), reservationCreateRequest.getSeatIds());
//		seatValidator.validateSeatsBelongToGame(reservationCreateRequest.getGameId(), reservationCreateRequest.getSeatIds());
//		seatValidator.checkDuplicateSeats(reservationCreateRequest.getGameId(), reservationCreateRequest.getSeatIds());

		//검증이 완료되면 좌석 점유
		seatHoldRedisUtil.holdSeatAtomic(reservationCreateRequest.getSeatIds(), reservationCreateRequest.getGameId(), String.valueOf(auth.getMemberId()));

		// todo : game 데이터 호출 use reservationCreateRequest.getGameId();
		GameDto gameDto = new GameDto();
		// todo : seat 데이터 호출 use reservationCreateRequest.getSeatIds();
		List<SeatDto> seats = new ArrayList<>();
		int seatPrice = seats.stream().mapToInt(SeatDto::getPrice).sum();

		Reservation reservation = new Reservation(auth.getMemberId(), gameDto.getId(), "WAITING_PAYMENT", seatPrice);
		for (SeatDto seat : seats) {
			reservation.addSeat(new ReserveSeat(seat.getSeatId()));
		}

		reservationRepository.save(reservation);

		return ReservationResponse.from(reservation, gameDto, seats);
	}

	@Transactional
	public TicketResponse completePaymentReservation(AuthUser authUser, Long reservationId) {
		Reservation reservation = reservationRepository.findByIdAndStateWithSeatsAndGame(reservationId, "WAITING_PAYMENT").orElseThrow();
		List<Long> seatIds = reservation.getReservations().stream().map(ReserveSeat::getSeatId).toList();
		Long gameId = reservation.getGameId();

		ReservationDto reservationDto = ReservationDto.from(reservation);

		//티켓 정보를 생성
		TicketResponse ticketResponse = ticketService.issueTicketFromReservation(authUser, reservationDto);

		reservation.completePayment();
		eventPublisher.publishEvent(new SeatHoldReleaseEvent(seatIds, gameId));

		return ticketResponse;
	}

}
