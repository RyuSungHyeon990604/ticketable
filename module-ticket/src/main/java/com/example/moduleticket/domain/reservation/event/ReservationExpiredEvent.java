package com.example.moduleticket.domain.reservation.event;

import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ReservationExpiredEvent {
	private final Long reservationId;
	private final Long memberId;
	private final Long gameId;
	private final List<Long> seatIds;
}
