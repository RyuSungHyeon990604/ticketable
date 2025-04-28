package com.example.moduleticket.domain.ticket.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.example.moduleticket.domain.ticket.entity.Ticket;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TicketDto {

	private Long id;
	private Long memberId;
	private Long gameId;
	private LocalDateTime deletedAt;
	private Integer totalPoint;
	private List<Long> seatIds;

	public static TicketDto from(Ticket ticket, Integer totalPoint, List<Long> seatIds) {
		return new TicketDto(
			ticket.getId(),
			ticket.getMemberId(),
			ticket.getGameId(),
			ticket.getDeletedAt(),
			totalPoint,
			seatIds
		);
	}
}
