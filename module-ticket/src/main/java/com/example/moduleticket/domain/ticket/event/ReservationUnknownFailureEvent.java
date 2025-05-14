package com.example.moduleticket.domain.ticket.event;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReservationUnknownFailureEvent {
	private Long reservationId;
	private List<Long> seatIds;
	private Long gameId;
	private Long memberId;
}
