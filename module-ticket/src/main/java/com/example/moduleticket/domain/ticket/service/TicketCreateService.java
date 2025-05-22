package com.example.moduleticket.domain.ticket.service;

import static com.example.moduleticket.global.exception.ErrorCode.RESERVATION_NOT_FOUND;

import com.example.moduleticket.domain.reservation.entity.Reservation;
import com.example.moduleticket.domain.reservation.repository.ReservationRepository;
import com.example.moduleticket.domain.ticket.dto.TicketContext;
import com.example.moduleticket.domain.ticket.entity.Ticket;
import com.example.moduleticket.domain.ticket.repository.TicketRepository;
import com.example.moduleticket.global.exception.ServerException;
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
	private final ReservationRepository reservationRepository;
	private final TicketSeatService ticketSeatService;

	@Transactional
	public TicketContext createTicket(Long memberId, List<Long> seats, Long reservationId) {
		Reservation reservation = reservationRepository.findById(reservationId)
			.orElseThrow(()->new ServerException(RESERVATION_NOT_FOUND));
		long gameId = reservation.getGameId();

		Ticket ticket = ticketRepository.save(new Ticket(reservation, memberId, gameId));
		ticketSeatService.createAll(seats, gameId, ticket);

		return new TicketContext(ticket, memberId, seats, reservation.getTotalPrice());
	}
}
