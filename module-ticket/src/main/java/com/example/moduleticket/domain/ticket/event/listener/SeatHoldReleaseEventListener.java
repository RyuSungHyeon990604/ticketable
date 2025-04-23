package com.example.moduleticket.domain.ticket.event.listener;

import com.example.moduleticket.domain.ticket.event.SeatHoldReleaseEvent;
import com.example.moduleticket.util.SeatHoldRedisUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class SeatHoldReleaseEventListener {
	private final SeatHoldRedisUtil seatHoldRedisUtil;

	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	public void handleSeatHoldReleaseEvent(SeatHoldReleaseEvent event) {
		log.debug(" {} 좌석 해제 이벤트 ",event.getSeatIds());
		seatHoldRedisUtil.releaseSeatAtomic(event.getSeatIds(), event.getGameId());
	}
}
