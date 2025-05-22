package com.example.moduleticket.domain.reservation.event.listener;

import com.example.moduleticket.domain.reservation.event.ReservationCreatedEvent;
import com.example.moduleticket.domain.reservation.event.TicketEvent;
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
public class ReservationCreatedEventListener {
	private final RedisTemplate<String, Object> redisTemplate;

	@Async("refreshCacheThreadPool")
	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	public void refreshCache(ReservationCreatedEvent event){
		log.info("Refresh cache");
		log.debug("예약 완료 캐시 갱신 수행 : reservationId = {}", event.getReservationId());
		TicketEvent ticketEvent = new TicketEvent(
			event.getGameId(),
			event.getSeatIds().get(0)
		);
		redisTemplate.convertAndSend("reservation", ticketEvent);
	}
}
