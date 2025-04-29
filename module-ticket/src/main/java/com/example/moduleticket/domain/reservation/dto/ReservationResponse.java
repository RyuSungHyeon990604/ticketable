package com.example.moduleticket.domain.reservation.dto;

import com.example.moduleticket.domain.reservation.entity.Reservation;
import com.example.moduleticket.feign.dto.GameDto;
import com.example.moduleticket.feign.dto.SeatDetailDto;
import com.example.moduleticket.feign.dto.SeatDto;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReservationResponse {
	private final Long reservationId;
	private final Long gameId;
	private final List<String> seats;
	private final LocalDateTime startTime;
	private final String state;
	private final Long memberId;

	public static ReservationResponse from(Reservation reservation, List<SeatDetailDto> seatDetailDto) {
		List<String> seats = seatDetailDto.stream().map(SeatDetailDto::getPosition).toList();
		return new ReservationResponse(
			reservation.getId(),
			reservation.getGameId(),
			seats,
			seatDetailDto.get(0).getStartTime(),
			reservation.getState(),
			reservation.getMemberId()
			);
	}
}
