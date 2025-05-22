package com.example.moduleticket.domain.ticket.service;


import static com.example.moduleticket.global.exception.ErrorCode.ALREADY_CANCELED_GAME;
import static com.example.moduleticket.global.exception.ErrorCode.TICKET_NOT_FOUND;
import static com.example.moduleticket.global.exception.ErrorCode.USER_ACCESS_DENIED;

import com.example.moduleticket.RefundQueueService;
import com.example.moduleticket.domain.reservation.entity.Reservation;
import com.example.moduleticket.domain.reservation.event.TicketEvent;
import com.example.moduleticket.domain.reservation.event.publisher.TicketPublisher;
import com.example.moduleticket.domain.ticket.dto.RefundDto;
import com.example.moduleticket.domain.ticket.dto.TicketContext;
import com.example.moduleticket.domain.ticket.dto.TicketDto;
import com.example.moduleticket.domain.ticket.dto.response.TicketResponse;
import com.example.moduleticket.domain.ticket.entity.Ticket;
import com.example.moduleticket.domain.ticket.entity.TicketSeat;
import com.example.moduleticket.domain.ticket.repository.TicketRepository;
import com.example.moduleticket.feign.GameClient;
import com.example.moduleticket.feign.dto.GameDto;
import com.example.moduleticket.global.argumentresolver.AuthUser;
import com.example.moduleticket.global.dto.ApiResponse;
import com.example.moduleticket.global.exception.ServerException;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@AllArgsConstructor
public class TicketService {

	private final TicketRepository ticketRepository;
	private final TicketSeatService ticketSeatService;
	private final TicketPaymentService ticketPaymentService;
	private final TicketCreateService ticketCreateService;
	private final GameClient gameClient;
	private final RefundQueueService refundQueueService;
	private final TicketPublisher ticketPublisher;


	@Transactional(readOnly = true)
	public ApiResponse<TicketResponse> getTicket(AuthUser auth, Long ticketId) {
		Ticket ticket = ticketRepository.findByIdAndDeletedAtIsNullWithReservation(ticketId, auth.getMemberId())
			.orElseThrow(() -> new ServerException(TICKET_NOT_FOUND));

		return ApiResponse.of(TicketResponse.from(ticket), "티켓 단건 조회") ;
	}

	//todo : n+1 문제 해결 필요
	@Transactional(readOnly = true)
	public ApiResponse<List<TicketResponse>> getAllTickets(AuthUser auth) {
		List<Ticket> allTickets = ticketRepository.findAllByMemberIdWithGame(auth.getMemberId());

		return ApiResponse.of(allTickets.stream().map(TicketResponse::from).toList(), "티켓 다건 조회") ;
	}

	@Transactional(readOnly = true)
	public ApiResponse<TicketResponse> getTicketByReservationId(Long reservationId) {
		Ticket ticket = ticketRepository.findByReservationId(reservationId)
			.orElseThrow(() -> new ServerException(TICKET_NOT_FOUND));
		return ApiResponse.of(TicketResponse.from(ticket), "티켓 조회 by 예약ID");
	}


	@Transactional
	public void issueTicketFromReservation(Long memberId, List<Long> seatIds, Long reservationId) {
		TicketContext ticketContext = ticketCreateService.createTicket(memberId, seatIds, reservationId);
		ticketPaymentService.create(
			ticketContext.getTicket(),
			ticketContext.getMemberId(),
			ticketContext.getTotalPoint()
		);
	}

	@Transactional
	public void cancelTicket(AuthUser auth, Long ticketId) {

		// 1. 티켓 취소 처리
		Ticket ticket = ticketRepository.findByIdAndDeletedAtIsNull(ticketId, auth.getMemberId())
			.orElseThrow(() -> new ServerException(TICKET_NOT_FOUND));
		if (auth.getRole().equals("AMDIN") && !auth.getMemberId().equals(ticket.getMemberId())) {
			throw new ServerException(USER_ACCESS_DENIED);
		}
		Long gameId = ticket.getGameId();
		Long seatId = ticketSeatService.getSeat(ticketId).get(0).getSeatId();
		ticket.cancel();

		// 2. 환불금 조회
		ticketPaymentService.refundPrice(auth.getMemberId(), ticket.getId());

		// 캐시 이벤트 발생
		TicketEvent ticketEvent = new TicketEvent(gameId, seatId);
		ticketPublisher.publish(ticketEvent);
	}

	/**
	 * 경기가 취소됐을때 모든 티켓을 취소 처리 해줘야 함
	 * @param gameId
	 */
	@Transactional
	public void deleteAllTicketsByCanceledGame(Long gameId) {
		List<RefundDto> refundDtoList = ticketRepository.findRefundDtoByGameId(gameId);
		if(refundDtoList == null || refundDtoList.isEmpty()){
			throw new ServerException(ALREADY_CANCELED_GAME);
		}
		refundQueueService.enqueueRefundTicket(refundDtoList);
		ticketRepository.softDeleteAllByGameId(gameId);
	}

	public TicketDto getTicketInternal(Long memberId, Long ticketId) {
		Ticket ticket = ticketRepository.findByIdAndDeletedAtIsNull(ticketId, memberId)
			.orElseThrow(() -> new ServerException(TICKET_NOT_FOUND));
		int ticketTotalPoint = ticketPaymentService.getTicketTotalPoint(ticketId);
		List<Long> ticketSeatIds = ticketSeatService.getSeat(ticketId).stream().map(TicketSeat::getSeatId).toList();

		return TicketDto.from(ticket, ticketTotalPoint, ticketSeatIds);
	}

	public List<TicketDto> getTicketsInternal(Long gameId) {
		return ticketRepository.findByGameId(gameId)
			.stream()
			.map(ticket -> TicketDto.from(ticket, null, null))
			.toList();
	}
}
