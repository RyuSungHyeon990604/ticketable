package com.example.moduleticket.domain.reservation.event.listener;

import com.example.moduleticket.domain.reservation.event.ReservationPaymentComplete;
import com.example.moduleticket.domain.reservation.event.TicketEvent;
import com.example.moduleticket.domain.ticket.service.TicketService;
import com.example.moduleticket.util.SeatHoldRedisUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReservationCompleteEventListener {
	private final TicketService ticketService;
	private final SeatHoldRedisUtil seatHoldRedisUtil;
	private final RedisTemplate<String, Object> redisTemplate;

	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	public void releaseSeatHoldFromRedis(ReservationPaymentComplete event) {
		log.debug("예약 결제 완료 좌석 점유 해제 : reservationId = {}", event.getReservationId());
		seatHoldRedisUtil.releaseSeatAtomic(event.getSeatIds(), event.getGameId());
	}

	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	public void issueTicket(ReservationPaymentComplete event) {
		try {
			ticketService.issueTicketFromReservation(
				event.getMemberId(),
				event.getSeatIds(),
				event.getReservationId()
			);
		} catch (Exception e) {
			log.error("티켓 발급 실패: reservationId={}, error={}", event.getReservationId(), e.getMessage(), e);
			// todo : 복구 처리 or 재시도 처리
		}
	}

	@Async("refreshCacheThreadPool")
	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	public void refreshCache(ReservationPaymentComplete event){
		log.info("예약 결제 완료 캐시갱신 수행 : reservationId = {}", event.getReservationId());
		TicketEvent ticketEvent = new TicketEvent(
			event.getGameId(),
			event.getSeatIds().get(0)
		);
		redisTemplate.convertAndSend("reservation", ticketEvent);
	}
}
