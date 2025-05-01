package com.example.moduleticket.domain.ticket.service;

import com.example.moduleticket.domain.reservation.entity.Reservation;
import com.example.moduleticket.domain.ticket.dto.TicketContext;
import com.example.moduleticket.domain.ticket.entity.Ticket;
import com.example.moduleticket.domain.ticket.repository.TicketRepository;
import com.example.moduleticket.feign.dto.SeatDetailDto;
import com.example.moduleticket.global.argumentresolver.AuthUser;
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
	public TicketContext createTicket(AuthUser auth, List<SeatDetailDto> seats, Reservation reservation) {
		long gameId = seats.get(0).getGameId();
		List<Long> seatIds = seats.stream().map(SeatDetailDto::getSeatId).toList();

		Ticket ticket = ticketRepository.save(new Ticket(reservation, auth.getMemberId(), gameId));
		ticketSeatService.createAll(seatIds, gameId, ticket);

		return new TicketContext(ticket, auth.getMemberId(), seats, reservation.getTotalPrice());
	}
}
