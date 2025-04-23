package com.example.moduleticket.domain.reservation.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReservationCreateRequest {
	private Long gameId;
	private List<Long> seatIds;
}
