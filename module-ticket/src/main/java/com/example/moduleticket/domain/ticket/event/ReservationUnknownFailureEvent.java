package com.example.moduleticket.domain.ticket.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReservationUnknownFailureEvent {
	private Long reservationId;
}
