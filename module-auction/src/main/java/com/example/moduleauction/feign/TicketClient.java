package com.example.moduleauction.feign;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.moduleauction.feign.dto.TicketDto;

@FeignClient(name = "ticket", url = "http://localhost:8082")
public interface TicketClient {

	@GetMapping("/api/internal/members/{memberId}/tickets/{ticketId}")
	TicketDto getTicket(
		@PathVariable("memberId") Long memberId,
		@PathVariable("ticketId") Long ticketId
	);

	@GetMapping("/api/internal/games/{gameId}/tickets")
	List<TicketDto> getTickets(@PathVariable("gameId") Long gameId);
}
