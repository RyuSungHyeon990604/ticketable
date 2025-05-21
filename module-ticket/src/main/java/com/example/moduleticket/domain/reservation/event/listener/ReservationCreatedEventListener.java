package com.example.moduleticket.domain.reservation.event.listener;

import com.example.moduleticket.domain.reservation.event.ReservationCompleteEvent;
import com.example.moduleticket.domain.reservation.event.ReservationCreatedEvent;
import com.example.moduleticket.domain.reservation.event.TicketEvent;
import com.example.moduleticket.util.SeatHoldRedisUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@RequiredArgsConstructor
public class ReservationCreatedEventListener {
	private final RedisTemplate<String, Object> redisTemplate;

	@Async
	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	public void refreshCache(ReservationCreatedEvent event){
		log.debug("예약 완료 캐시 갱신 수행 : reservationId = {}", event.getReservationId());
		TicketEvent ticketEvent = new TicketEvent(
			event.getGameId(),
			event.getSeatIds().get(0)
		);
		redisTemplate.convertAndSend("reservation", ticketEvent);
	}
}
