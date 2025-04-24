package com.example.moduleticket.domain.reservation.dto;

import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReservationCreateRequest {

	@NotNull
	private Long gameId;

	@NotNull
	private List<Long> seatIds;
}
