package com.example.moduleticket.domain.reservation.event.listener;

import com.example.moduleticket.domain.reservation.event.ReservationExpiredEvent;
import com.example.moduleticket.domain.reservation.event.TicketEvent;
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
public class ReservationExpiredEventListener {
	private final SeatHoldRedisUtil seatHoldRedisUtil;
	private final RedisTemplate<String, Object> redisTemplate;

	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	public void releaseSeatHoldFromRedis(ReservationExpiredEvent event) {
		log.debug("예약 만료 좌석점유 해제 : reservationId = {}", event.getReservationId());
		seatHoldRedisUtil.releaseSeatAtomic(event.getSeatIds(), event.getGameId());
	}

	@Async("refreshCacheThreadPool")
	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	public void refreshCache(ReservationExpiredEvent event){
		log.debug("예약 만료 캐시 갱신 수행 : reservationId = {}", event.getReservationId());
		TicketEvent ticketEvent = new TicketEvent(
			event.getGameId(),
			event.getSeatIds().get(0)
		);
		redisTemplate.convertAndSend("reservation", ticketEvent);
	}
}
