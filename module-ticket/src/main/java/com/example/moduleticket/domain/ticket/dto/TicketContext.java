package com.example.moduleticket.domain.ticket.dto;

import com.example.moduleticket.domain.ticket.dto.response.TicketResponse;
import com.example.moduleticket.domain.ticket.entity.Ticket;
import com.example.moduleticket.feign.dto.SeatDetailDto;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TicketContext {
	private final Ticket ticket;
	private final Long memberId;
	private final List<SeatDetailDto> seatDetailDtos;
	private final int totalPoint;

	public TicketResponse toResponse() {
		String title = seatDetailDtos.get(0).getHome() + " vs " + seatDetailDtos.get(0).getAway();
		return new TicketResponse(
			ticket.getId(),
			title,
			seatDetailDtos.stream().map(SeatDetailDto::getPosition).toList(),
			seatDetailDtos.get(0).getStartTime(),
			totalPoint
		);
	}
}
