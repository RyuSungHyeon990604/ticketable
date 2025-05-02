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
import com.example.moduleticket.feign.dto.SeatDetailDto;
import com.example.moduleticket.global.argumentresolver.AuthUser;
import com.example.moduleticket.global.exception.ServerException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
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
	public TicketResponse getTicket(AuthUser auth, Long ticketId) {
		Ticket ticket = ticketRepository.findByIdAndDeletedAtIsNull(ticketId, auth.getMemberId())
			.orElseThrow(() -> new ServerException(TICKET_NOT_FOUND));
		Long gameId = ticket.getGameId();
		GameDto gameDto = gameClient.getGame(gameId);

		return convertTicketResponse(ticket, gameDto);
	}

	@Transactional(readOnly = true)
	public List<TicketResponse> getAllTickets(AuthUser auth) {
		List<Ticket> allTickets = ticketRepository.findAllByMemberIdWithGame(auth.getMemberId());

		List<Long> gameIds = allTickets.stream().map(Ticket::getGameId).toList();
		List<GameDto> gameDtos = gameClient.getGames(gameIds);
		Map<Long, GameDto> gameDtoMap = gameDtos.stream()
			.collect(Collectors.toMap(GameDto::getId, Function.identity()));

		return allTickets.stream().map(ticket -> convertTicketResponse(ticket, gameDtoMap.get(ticket.getGameId())))
			.toList();
	}

	@Transactional(readOnly = true)
	public TicketResponse getTicketByReservationId(Long reservationId) {
		Ticket ticket = ticketRepository.findByReservationId(reservationId)
			.orElseThrow(() -> new ServerException(TICKET_NOT_FOUND));
		GameDto game = gameClient.getGame(ticket.getGameId());
		return convertTicketResponse(ticket, game);
	}


	@Transactional
	public TicketResponse issueTicketFromReservation(AuthUser auth, Long gameId, List<Long> seatIds,
		Reservation reservation) {

		List<SeatDetailDto> seatDetailDtos = gameClient.getSeatsByGame(gameId, seatIds);

		TicketContext ticketContext = ticketCreateService.createTicket(auth, seatDetailDtos, reservation);
		ticketPaymentService.create(
			ticketContext.getTicket(),
			ticketContext.getMemberId(),
			ticketContext.getTotalPoint()
		);

		return ticketContext.toResponse();
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
		GameDto game = gameClient.getGame(gameId);
		if(game.getDeletedAt() != null ){
			throw new ServerException(ALREADY_CANCELED_GAME);
		}
		List<RefundDto> refundDtoList = ticketRepository.findRefundDtoByGameId(gameId);
		refundQueueService.enqueueRefundTicket(refundDtoList);
		ticketRepository.softDeleteAllByGameId(gameId);
	}

	/**
	 * Ticket 을 기준으로 TicketResponse에 필요한 데이터를 가져오고 매핑하는 메서드
	 *
	 * @param ticket 엔티티 객체
	 * @return TicketResponse
	 */
	private TicketResponse convertTicketResponse(Ticket ticket, GameDto game) {
		String title = game.getHome() + " vs " + game.getAway();
		log.debug("경기 제목 조회 title: {}", title);

		LocalDateTime startTime = game.getStartTime();
		log.debug("경기 시작 시간 조회 startTime : {}", startTime);

		List<String> ticketSeats = ticketSeatService.getPositionsByTicketSeatId(game.getId(), ticket.getId());

		log.debug("티켓 좌석 조회 ticketSeats: {}", ticketSeats);

		int totalPoint = ticket.getReservation().getTotalPrice();

		log.debug("티켓 결제 금액 조회 ticketPayment: {}", totalPoint);

		return new TicketResponse(ticket.getId(), title, ticketSeats, startTime, totalPoint);
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
