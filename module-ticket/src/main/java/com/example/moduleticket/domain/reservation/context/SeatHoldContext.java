package com.example.moduleticket.domain.reservation.context;

import com.example.moduleticket.util.SeatHoldRedisUtil;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class SeatHoldContext implements AutoCloseable {
	private final Long gameId;
	private final List<Long> seatIds;
	private final SeatHoldRedisUtil redisUtil;
	private boolean shouldRelease = true;

	public void markReservationSuccess() {
		this.shouldRelease = false;
	}

	@Override
	public void close() {
		if(shouldRelease) {
			log.debug("좌석 점유 실패 : gameId={}, seatIds={}", gameId, seatIds);
			redisUtil.releaseSeatAtomic(seatIds, gameId);
		} else {
			log.debug("좌석 점유 성공 : gameId={}, seatIds={}", gameId, seatIds);
		}
	}
}
