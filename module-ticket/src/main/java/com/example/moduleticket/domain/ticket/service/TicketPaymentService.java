package com.example.moduleticket.domain.ticket.service;

import static com.example.modulecommon.exception.ErrorCode.TICKET_PAYMENT_NOT_FOUND;

import com.example.modulecommon.exception.ServerException;
import com.example.moduleticket.domain.point.enums.PointHistoryType;
import com.example.moduleticket.domain.reservation.dto.ReservationDto;
import com.example.moduleticket.domain.ticket.dto.TicketContext;
import com.example.moduleticket.domain.ticket.entity.Ticket;
import com.example.moduleticket.domain.ticket.entity.TicketPayment;
import com.example.moduleticket.domain.ticket.repository.TicketPaymentRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class TicketPaymentService {

	private final TicketPaymentRepository ticketPaymentRepository;

	public void create(Ticket ticket, Long memberId, int point) {
		TicketPayment ticketPayment = new TicketPayment(point, ticket, memberId);
		ticketPaymentRepository.save(ticketPayment);
	}

	public int getTicketTotalPoint(Long ticketId) {
		return ticketPaymentRepository.findByTicketId(ticketId)
			.orElseThrow(() -> new ServerException(TICKET_PAYMENT_NOT_FOUND))
			.getTotalPoint();
	}
}
