package com.example.moduleticket.domain.reservation.service;

import com.example.moduleticket.domain.reservation.dto.ReservationCreateRequest;
import com.example.moduleticket.domain.reservation.entity.Reservation;
import com.example.moduleticket.domain.reservation.entity.ReserveSeat;
import com.example.moduleticket.domain.reservation.repository.ReservationRepository;
import com.example.moduleticket.feign.GameClient;
import com.example.moduleticket.feign.dto.SeatDetailDto;
import com.example.moduleticket.global.argumentresolver.AuthUser;
import com.example.moduleticket.global.dto.ApiResponse;
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
	private final GameClient gameClient;

	@Transactional
	public ApiResponse<Void> createReservation(AuthUser auth, ReservationCreateRequest reservationCreateRequest) {

		reservationValidator.checkTicketSeatDuplicate(reservationCreateRequest.getGameId(), reservationCreateRequest.getSeatIds());
		reservationValidator.checkDuplicateReservation(reservationCreateRequest.getSeatIds(),reservationCreateRequest.getGameId() );

		List<SeatDetailDto> seatDetailDtos = gameClient.getSeatsByGameAndSection(
			reservationCreateRequest.getGameId(),
			reservationCreateRequest.getSectionId(),
			reservationCreateRequest.getSeatIds()
		);

		int seatPrice = seatDetailDtos.stream().mapToInt(SeatDetailDto::getSectionPrice).sum();
		int gamePrice = seatDetailDtos.get(0).getGamePrice();

		//예약 데이터 생성
		Reservation reservation = new Reservation(
			auth.getMemberId(),
			reservationCreateRequest.getGameId(),
			"WAITING_PAYMENT",
			seatPrice + gamePrice
		);

		List<ReserveSeat> ReserveSeatList = ReserveSeat.from(
			reservationCreateRequest.getSectionId(),
			reservationCreateRequest.getSeatIds()
		);

		for (ReserveSeat reserveSeat : ReserveSeatList) {
			reservation.addSeat(reserveSeat);
		}
		reservationRepository.save(reservation);

		return ApiResponse.messageOnly("예약이 완료되었습니다.");
	}
}
