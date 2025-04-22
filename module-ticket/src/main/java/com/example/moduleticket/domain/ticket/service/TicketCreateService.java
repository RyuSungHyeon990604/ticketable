package com.example.moduleticket.domain.ticket.service;

import static com.example.modulecommon.exception.ErrorCode.GAME_NOT_FOUND;

import com.example.modulecommon.entity.AuthUser;
import com.example.modulecommon.exception.ServerException;
import com.example.moduleticket.domain.member.entity.Member;
import com.example.moduleticket.domain.ticket.dto.GameDto;
import com.example.moduleticket.domain.ticket.dto.SeatDto;
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

	@Transactional
	public TicketContext createTicketV2(AuthUser auth, TicketCreateRequest ticketCreateRequest) {

		//todo : api 호출해야함
		List<SeatDto> seats = new ArrayList<>();
		GameDto game = new GameDto();

		//todo : 인증서버가 주는거 받기
		Long memberId = 1L;

		Ticket ticket = ticketRepository.save(new Ticket(memberId, game.getId()));
		ticketSeatService.createAll(seats, game, ticket);

		int totalPrice = ticketPriceCalculator.calculateTicketPrice(game, seats);

		return new TicketContext(ticket, memberId, game, seats, totalPrice);
	}
}
