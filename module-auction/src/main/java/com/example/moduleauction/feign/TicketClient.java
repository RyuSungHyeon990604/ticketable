package com.example.moduleauction.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.moduleauction.feign.dto.TicketDto;

@FeignClient(name = "ticket", url = "http://localhost:8082")
public interface TicketClient {

	@GetMapping("/Internal/members/{memberId}/tickets/{ticketId}")
	TicketDto getTicket(@PathVariable Long memberId, Long ticketId);
}
