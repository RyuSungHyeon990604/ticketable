package com.example.moduleticket.domain.ticket.service;

import static com.example.modulecommon.exception.ErrorCode.TICKET_ALREADY_RESERVED;

import com.example.modulecommon.exception.ServerException;
import com.example.moduleticket.feign.SeatService;
import com.example.moduleticket.feign.dto.GameDto;
import com.example.moduleticket.feign.dto.SeatDto;
import com.example.moduleticket.domain.ticket.entity.Ticket;
import com.example.moduleticket.domain.ticket.entity.TicketSeat;
import com.example.moduleticket.domain.ticket.repository.TicketSeatRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TicketSeatService {
	private final TicketSeatRepository ticketSeatRepository;
	private final SeatService seatService;

	public void createAll(List<SeatDto> seats, GameDto game, Ticket ticket) {
		List<TicketSeat> ticketSeats = seats.stream().map(seat -> new TicketSeat(ticket, seat.getSeatId(), game.getId())).toList();
		ticketSeatRepository.saveAll(ticketSeats);
	}

	public void checkDuplicateSeats(List<Long> seatIds, Long gameId) {
		if(ticketSeatRepository.existsByGameIdAndSeatIdInAndTicketDeletedAtIsNull(gameId, seatIds)) {
			log.debug("이미 예매된 좌석입니다.");
			throw new ServerException(TICKET_ALREADY_RESERVED);
		}
	}

	public List<SeatDto> getSeatByTicketSeatId(Long ticketId) {
		List<TicketSeat> ticketSeats = ticketSeatRepository.findByTicketId(ticketId);
		List<Long> seatIds = ticketSeats.stream().map(TicketSeat::getSeatId).toList();
		List<SeatDto> seatDtos = seatService.getSeats(seatIds);

		return  seatDtos;
	}

	public void deleteAllTicketSeats(Long ticketId) {
		ticketSeatRepository.deleteAllByTicketId(ticketId);
	}

	public List<TicketSeat> getSeat(Long ticketId) {
		return ticketSeatRepository.findByTicketId(ticketId);
	}
}
