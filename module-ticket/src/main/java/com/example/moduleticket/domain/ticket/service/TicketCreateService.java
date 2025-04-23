package com.example.moduleticket.domain.ticket.service;

import com.example.modulecommon.entity.AuthUser;
import com.example.moduleticket.feign.GameClient;
import com.example.moduleticket.domain.reservation.dto.ReservationDto;
import com.example.moduleticket.feign.SeatClient;
import com.example.moduleticket.feign.dto.GameDto;
import com.example.moduleticket.feign.dto.SeatDto;
import com.example.moduleticket.domain.ticket.dto.TicketContext;
import com.example.moduleticket.domain.ticket.entity.Ticket;
import com.example.moduleticket.domain.ticket.repository.TicketRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service
@RequiredArgsConstructor
public class TicketCreateService {

	private final TicketRepository ticketRepository;
	private final TicketSeatService ticketSeatService;

	@Transactional
	public TicketContext createTicket(AuthUser auth, GameDto game, List<SeatDto> seats, int totalPrice) {

		Ticket ticket = ticketRepository.save(new Ticket(auth.getMemberId(), game.getId()));
		ticketSeatService.createAll(seats, game, ticket);

		return new TicketContext(ticket, auth.getMemberId(), game, seats, totalPrice);
	}
}
