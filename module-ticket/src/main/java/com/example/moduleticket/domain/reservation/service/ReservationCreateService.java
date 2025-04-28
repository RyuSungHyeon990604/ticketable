package com.example.moduleticket.domain.reservation.service;

import com.example.modulecommon.entity.AuthUser;
import com.example.moduleticket.domain.reservation.dto.ReservationCreateRequest;
import com.example.moduleticket.domain.reservation.dto.ReservationResponse;
import com.example.moduleticket.domain.reservation.entity.Reservation;
import com.example.moduleticket.domain.reservation.entity.ReserveSeat;
import com.example.moduleticket.domain.reservation.repository.ReservationRepository;
import com.example.moduleticket.feign.GameClient;
import com.example.moduleticket.feign.SeatClient;
import com.example.moduleticket.feign.dto.GameDto;
import com.example.moduleticket.feign.dto.SeatDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReservationCreateService {
	private final ReservationRepository reservationRepository;
	private final ReservationValidator reservationValidator;
	private final GameClient gameClient;
	private final SeatClient seatClient;

	@Transactional
	public ReservationResponse createReservation(AuthUser auth, ReservationCreateRequest reservationCreateRequest) {

		reservationValidator.checkTicketSeatDuplicate(reservationCreateRequest.getGameId(), reservationCreateRequest.getSeatIds());
		reservationValidator.checkDuplicateReservation(reservationCreateRequest.getSeatIds());

		GameDto gameDto = gameClient.getGame(reservationCreateRequest.getGameId());
		List<SeatDto> seats = seatClient.getSeatsByGameAndSection(
			reservationCreateRequest.getGameId(),
			reservationCreateRequest.getSectionId(),
			reservationCreateRequest.getSeatIds()
		);

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
}
