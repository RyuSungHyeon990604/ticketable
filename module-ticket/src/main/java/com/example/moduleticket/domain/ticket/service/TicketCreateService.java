package com.example.moduleticket.domain.ticket.service;

import static com.example.modulecommon.exception.ErrorCode.GAME_NOT_FOUND;

import com.example.modulecommon.exception.ServerException;
import com.example.moduleticket.domain.member.entity.Member;
import com.example.moduleticket.domain.ticket.dto.TicketContext;
import com.example.moduleticket.domain.ticket.dto.request.TicketCreateRequest;
import com.example.moduleticket.domain.ticket.entity.Ticket;
import com.example.moduleticket.domain.ticket.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service
@RequiredArgsConstructor
public class TicketCreateService {

	private final TicketRepository ticketRepository;
	private final GameRepository gameRepository;
	private final TicketSeatService ticketSeatService;
	private final SeatService seatService;
	private final TicketPriceCalculator ticketPriceCalculator;

	@Transactional
	public TicketContext createTicketV2(Auth auth, TicketCreateRequest ticketCreateRequest) {

		List<Seat> seats = seatService.getAllSeatEntity(ticketCreateRequest.getSeats());
		Game game = gameRepository.findById(ticketCreateRequest.getGameId()).orElseThrow(()->new ServerException(GAME_NOT_FOUND));

		Member member = Member.fromAuth(auth.getId());
		Ticket ticket = ticketRepository.save(new Ticket(member, game));
		ticketSeatService.createAll(seats, game, ticket);

		int totalPrice = ticketPriceCalculator.calculateTicketPrice(game, seats);

		return new TicketContext(ticket, member, game, seats, totalPrice);
	}
}
