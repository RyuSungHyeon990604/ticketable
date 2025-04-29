package com.example.moduleticket.domain.ticket.service;

import com.example.moduleticket.feign.SeatClient;
import com.example.moduleticket.feign.dto.SeatDetailDto;
import com.example.moduleticket.feign.dto.SeatDto;
import com.example.moduleticket.domain.ticket.entity.Ticket;
import com.example.moduleticket.domain.ticket.entity.TicketSeat;
import com.example.moduleticket.domain.ticket.repository.TicketSeatRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TicketSeatService {
	private final TicketSeatRepository ticketSeatRepository;
	private final SeatClient seatService;

	public void createAll(List<Long> seatIds, long gameId, Ticket ticket) {
		List<TicketSeat> ticketSeats = seatIds.stream().map(seatId -> new TicketSeat(ticket, seatId, gameId)).toList();
		ticketSeatRepository.saveAll(ticketSeats);
	}

	public List<String> getPositionsByTicketSeatId(Long gameId, Long ticketId) {
		List<TicketSeat> ticketSeats = ticketSeatRepository.findByTicketId(ticketId);
		List<Long> seatIds = ticketSeats.stream().map(TicketSeat::getSeatId).toList();
		List<SeatDetailDto> seatDtos = seatService.getSeatsByGame(gameId, seatIds);

		return  seatDtos.stream().map(SeatDetailDto::getPosition).toList();
	}

	public void deleteAllTicketSeats(Long ticketId) {
		ticketSeatRepository.deleteAllByTicketId(ticketId);
	}

	public List<TicketSeat> getSeat(Long ticketId) {
		return ticketSeatRepository.findByTicketId(ticketId);
	}
}
