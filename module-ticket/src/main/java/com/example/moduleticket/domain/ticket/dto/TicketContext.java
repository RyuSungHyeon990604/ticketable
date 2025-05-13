package com.example.moduleticket.domain.ticket.dto;

import com.example.moduleticket.domain.ticket.entity.Ticket;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TicketContext {
	private final Ticket ticket;
	private final Long memberId;
	private final List<Long> seatIds;
	private final int totalPoint;
}
