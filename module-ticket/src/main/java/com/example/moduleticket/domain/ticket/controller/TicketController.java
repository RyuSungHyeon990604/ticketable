package com.example.moduleticket.domain.ticket.controller;

import com.example.moduleticket.domain.ticket.dto.request.TicketCreateRequest;
import com.example.moduleticket.domain.ticket.dto.response.TicketResponse;
import com.example.moduleticket.domain.ticket.service.TicketService;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class TicketController {
	private final TicketService ticketService;


	@GetMapping("/v1/tickets")
	public ResponseEntity<List<TicketResponse>> getAllTickets(
		//@AuthenticationPrincipal Auth auth
	) {
		List<TicketResponse> ticketResponseList = ticketService.getAllTickets(auth);
		return ResponseEntity.ok(ticketResponseList);
	}

	@GetMapping("/v1/tickets/{ticketId}")
	public ResponseEntity<TicketResponse> getTicket(
		//@AuthenticationPrincipal Auth auth,
		@PathVariable Long ticketId
	) {
		TicketResponse ticketResponse = ticketService.getTicket(auth, ticketId);
		return ResponseEntity.ok(ticketResponse);
	}

	@PostMapping("/v3/tickets")
	public ResponseEntity<TicketResponse> createTicketV3(//@AuthenticationPrincipal Auth auth,
		@RequestBody TicketCreateRequest ticketCreateRequest) {
		TicketResponse ticketResponse = ticketService.reservationTicketV4(auth, ticketCreateRequest);
		return ResponseEntity.ok().body(ticketResponse);
	}

	@DeleteMapping("/v1/tickets/{ticketId}")
	public ResponseEntity<Void> deleteTicket(//@AuthenticationPrincipal Auth auth,
		@PathVariable Long ticketId) {
		ticketService.cancelTicket(auth, ticketId);

		return ResponseEntity.noContent().build();
	}
}
