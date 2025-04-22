package com.example.moduleticket.domain.ticket.dto;

import com.example.moduleticket.domain.member.entity.Member;
import com.example.moduleticket.domain.ticket.dto.response.TicketResponse;
import com.example.moduleticket.domain.ticket.entity.Ticket;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TicketContext {
	private final Ticket ticket;
	private final Member member;
	private final Game game;
	private final List<Seat> seats;
	private final int totalPoint;

	public TicketResponse toResponse() {
		return new TicketResponse(
			ticket.getId(),
			game.getHome() + " vs " + game.getAway(),
			seats.stream().map(Seat::getPosition).toList(),
			game.getStartTime(),
			totalPoint
		);
	}
}
