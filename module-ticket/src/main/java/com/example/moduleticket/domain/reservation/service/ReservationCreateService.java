package com.example.moduleticket.domain.reservation.service;

import com.example.moduleticket.domain.reservation.dto.ReservationCreateRequest;
import com.example.moduleticket.domain.reservation.dto.ReservationResponse;
import com.example.moduleticket.domain.reservation.entity.Reservation;
import com.example.moduleticket.domain.reservation.entity.ReserveSeat;
import com.example.moduleticket.domain.reservation.repository.ReservationRepository;
import com.example.moduleticket.feign.SeatClient;
import com.example.moduleticket.feign.dto.SeatDetailDto;
import com.example.moduleticket.global.argumentresolver.AuthUser;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationCreateService {
	private final ReservationRepository reservationRepository;
	private final ReservationValidator reservationValidator;
	private final SeatClient seatClient;

	@Transactional
	public ReservationResponse createReservation(AuthUser auth, ReservationCreateRequest reservationCreateRequest) {

		reservationValidator.checkTicketSeatDuplicate(reservationCreateRequest.getGameId(), reservationCreateRequest.getSeatIds());
		reservationValidator.checkDuplicateReservation(reservationCreateRequest.getSeatIds());

		List<SeatDetailDto> seatDetailDtos = seatClient.getSeatsByGameAndSection(
			reservationCreateRequest.getGameId(),
			reservationCreateRequest.getSectionId(),
			reservationCreateRequest.getSeatIds()
		);

		int seatPrice = seatDetailDtos.stream().mapToInt(seat-> seat.getSectionPrice() + seat.getGamePrice()).sum();

		//예약 데이터 생성
		Reservation reservation = new Reservation(
			auth.getMemberId(),
			reservationCreateRequest.getGameId(),
			"WAITING_PAYMENT",
			seatPrice
		);

		List<ReserveSeat> ReserveSeatList = ReserveSeat.from(
			reservationCreateRequest.getSectionId(),
			reservationCreateRequest.getSeatIds()
		);

		for (ReserveSeat reserveSeat : ReserveSeatList) {
			reservation.addSeat(reserveSeat);
		}
		reservationRepository.save(reservation);

		return ReservationResponse.from(reservation, seatDetailDtos);
	}
}
