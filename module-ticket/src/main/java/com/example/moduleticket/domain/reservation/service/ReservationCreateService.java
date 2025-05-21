package com.example.moduleticket.domain.reservation.service;

import com.example.grpc.game.SeatDetailDto;
import com.example.grpc.game.SeatDetailDtoList;
import com.example.moduleticket.domain.reservation.dto.ReservationCreateRequest;
import com.example.moduleticket.domain.reservation.entity.Reservation;
import com.example.moduleticket.domain.reservation.entity.ReserveSeat;
import com.example.moduleticket.domain.reservation.enums.ReservationState;
import com.example.moduleticket.domain.reservation.repository.ReservationRepository;
import com.example.moduleticket.feign.GameClient;
import com.example.moduleticket.global.argumentresolver.AuthUser;
import com.example.moduleticket.global.dto.ApiResponse;
import com.example.moduleticket.grpc.GameGrpcClient;
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
	private final GameGrpcClient gameGrpcClient;

	@Transactional
	public Reservation createReservation(AuthUser auth, ReservationCreateRequest reservationCreateRequest) {

		//좌석 중복 검증
		reservationValidator.checkTicketSeatDuplicate(reservationCreateRequest.getGameId(), reservationCreateRequest.getSeatIds());
		reservationValidator.checkDuplicateReservation(reservationCreateRequest.getSeatIds(),reservationCreateRequest.getGameId() );

		//좌석 정보를 가져온다
//		List<SeatDetailDto> seatDetailDtos = gameClient.getSeatsByGameAndSection(
//			reservationCreateRequest.getGameId(),
//			reservationCreateRequest.getSectionId(),
//			reservationCreateRequest.getSeatIds()
//		);
//
//		int seatPrice = seatDetailDtos.stream().mapToInt(SeatDetailDto::getSectionPrice).sum();
//		int gamePrice = seatDetailDtos.get(0).getGamePrice();

		SeatDetailDtoList seatDetail = gameGrpcClient.getSeatDetail(
			reservationCreateRequest.getGameId(),
			reservationCreateRequest.getSectionId(),
			reservationCreateRequest.getSeatIds()
		);

		int seatPrice = seatDetail.getSeatDetailDtoListList().stream().mapToInt(SeatDetailDto::getSectionPrice).sum();
		int gamePrice = seatDetail.getSeatDetailDtoList(0).getGamePrice();


		//예약 데이터 생성
		Reservation reservation = new Reservation(
			auth.getMemberId(),
			reservationCreateRequest.getGameId(),
			ReservationState.WAITING_PAYMENT,
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

		return reservation;
	}
}
