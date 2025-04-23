package com.example.moduleticket.domain.ticket.dto;

import com.example.moduleticket.feign.dto.GameDto;
import com.example.moduleticket.feign.dto.SeatDto;
import com.example.moduleticket.domain.ticket.dto.response.TicketResponse;
import com.example.moduleticket.domain.ticket.entity.Ticket;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TicketContext {
	private final Ticket ticket;
	private final Long memberId;
	private final GameDto game;
	private final List<SeatDto> seats;
	private final int totalPoint;

	public TicketResponse toResponse() {
		return new TicketResponse(
			ticket.getId(),
			game.getHome() + " vs " + game.getAway(),
			seats.stream().map(SeatDto::getPosition).toList(),
			game.getStartTime(),
			totalPoint
		);
	}
}
