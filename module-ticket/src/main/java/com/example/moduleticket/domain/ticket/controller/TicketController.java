package com.example.moduleticket.domain.ticket.controller;

import com.example.moduleticket.domain.ticket.dto.TicketDto;
import com.example.moduleticket.domain.ticket.dto.response.TicketResponse;
import com.example.moduleticket.domain.ticket.service.TicketService;
import com.example.moduleticket.global.annotation.LoginUser;
import com.example.moduleticket.global.argumentresolver.AuthUser;
import com.example.moduleticket.global.dto.ApiResponse;
import java.util.List;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class TicketController {
	private final TicketService ticketService;


	@GetMapping("/v1/tickets")
	public ResponseEntity<ApiResponse<?>> getAllTickets(
		@LoginUser AuthUser authUser
	) {
		ApiResponse<List<TicketResponse>> allTickets = ticketService.getAllTickets(authUser);
		return ResponseEntity.ok(allTickets);
	}

	@GetMapping("/v1/tickets/{ticketId}")
	public ResponseEntity<ApiResponse<?>> getTicket(
		@LoginUser AuthUser authUser,
		@PathVariable Long ticketId
	) {
		ApiResponse<TicketResponse> ticketResponse = ticketService.getTicket(authUser, ticketId);
		return ResponseEntity.ok(ticketResponse);
	}


	@DeleteMapping("/v1/tickets/{ticketId}")
	public ResponseEntity<Void> deleteTicket(
		@LoginUser AuthUser authUser,
		@PathVariable Long ticketId) {
		ticketService.cancelTicket(authUser, ticketId);

		return ResponseEntity.noContent().build();
	}

	@DeleteMapping("/tickets/games/{gameId}")
	public ResponseEntity<Void> deleteTicketsByGameId(@PathVariable Long gameId) {
		ticketService.deleteAllTicketsByCanceledGame(gameId);
		return ResponseEntity.ok().build();
	}

	@GetMapping("/internal/members/{memberId}/tickets/{ticketId}")
	public ResponseEntity<TicketDto> getTicketInternal(
		@PathVariable("memberId") Long memberId,
		@PathVariable("ticketId") Long ticketId
	) {
		return ResponseEntity.ok(ticketService.getTicketInternal(memberId, ticketId));
	}

	@GetMapping("/Internal/games/{gameId}/tickets")
	public ResponseEntity<List<TicketDto>> getTickets(
		@PathVariable Long gameId
	) {
		return ResponseEntity.ok(ticketService.getTicketsInternal(gameId));
	}
}
