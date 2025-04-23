package com.example.moduleticket.domain.ticket.service;

import com.example.modulecommon.entity.AuthUser;
import com.example.moduleticket.feign.GameService;
import com.example.moduleticket.domain.reservation.dto.ReservationDto;
import com.example.moduleticket.feign.SeatService;
import com.example.moduleticket.feign.dto.GameDto;
import com.example.moduleticket.feign.dto.SeatDto;
import com.example.moduleticket.domain.ticket.dto.TicketContext;
import com.example.moduleticket.domain.ticket.dto.request.TicketCreateRequest;
import com.example.moduleticket.domain.ticket.entity.Ticket;
import com.example.moduleticket.domain.ticket.repository.TicketRepository;
import java.util.ArrayList;
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
	private final TicketPriceCalculator ticketPriceCalculator;
	private final GameService gameService;
	private final SeatService seatService;

	@Transactional
	public TicketContext createTicketV2(AuthUser auth, TicketCreateRequest ticketCreateRequest) {

		List<SeatDto> seats = seatService.getSeats(ticketCreateRequest.getSeats());
		GameDto game = gameService.getGame(ticketCreateRequest.getGameId());
		Long memberId = auth.getMemberId();

		Ticket ticket = ticketRepository.save(new Ticket(memberId, game.getId()));
		ticketSeatService.createAll(seats, game, ticket);

		int totalPrice = ticketPriceCalculator.calculateTicketPrice(game, seats);

		return new TicketContext(ticket, memberId, game, seats, totalPrice);
	}


	@Transactional
	public TicketContext createTicketV3(AuthUser auth, ReservationDto reservationDto) {

		//todo : api 호출해야함
		List<SeatDto> seats = seatService.getSeats(reservationDto.getSeatIds());
		GameDto game = gameService.getGame(reservationDto.getGameId());

		Ticket ticket = ticketRepository.save(new Ticket(auth.getMemberId(), game.getId()));
		ticketSeatService.createAll(seats, game, ticket);

		return new TicketContext(ticket, auth.getMemberId(), game, seats, reservationDto.getTotalPrice());
	}
}
