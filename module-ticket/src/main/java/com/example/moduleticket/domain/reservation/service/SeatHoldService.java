package com.example.moduleticket.domain.reservation.service;

import com.example.moduleticket.domain.reservation.context.SeatHoldContext;
import com.example.moduleticket.util.SeatHoldRedisUtil;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SeatHoldService {
	private final SeatHoldRedisUtil seatHoldRedisUtil;

	public SeatHoldContext hold(Long memberId, Long gameId, List<Long> seatIds) {
		seatHoldRedisUtil.holdSeatAtomic(gameId, seatIds, String.valueOf(memberId));
		return new SeatHoldContext(gameId,seatIds,seatHoldRedisUtil);
	}
}
