package com.example.moduleticket.domain.reservation.dto;

import com.example.moduleticket.domain.reservation.entity.Reservation;
import com.example.moduleticket.domain.reservation.entity.ReserveSeat;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReservationDto {
	private final Long id;
	private final List<Long> seatIds;
	private final Long gameId;
	private final int price;
	private final Long memberId;
	private final int totalPrice;

	public static ReservationDto from(Reservation reservation) {
		List<Long> seatIds = reservation.getReserveSeats().stream().map(ReserveSeat::getSeatId).toList();
		return new ReservationDto(
			reservation.getId(),
			seatIds,
			reservation.getGameId(),
			reservation.getTotalPrice(),
			reservation.getMemberId(),
			reservation.getTotalPrice()
		);
	}
}
