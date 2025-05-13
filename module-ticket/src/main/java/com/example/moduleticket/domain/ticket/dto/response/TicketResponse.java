package com.example.moduleticket.domain.ticket.dto.response;

import com.example.moduleticket.domain.reservation.entity.ReserveSeat;
import com.example.moduleticket.domain.ticket.entity.Ticket;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TicketResponse {
	private final Long ticketId;
	private final List<Long> seats;
	private final Integer totalPoint;

	public static TicketResponse from(Ticket ticket) {
		List<Long> seats = ticket.getReservation().getReserveSeats().stream().map(ReserveSeat::getSeatId).toList();
		int totalPoint = ticket.getReservation().getTotalPrice();
		return new TicketResponse(ticket.getId(), seats, totalPoint);
	}
}
