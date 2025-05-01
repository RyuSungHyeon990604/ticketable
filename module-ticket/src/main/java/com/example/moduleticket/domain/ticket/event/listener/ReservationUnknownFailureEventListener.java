package com.example.moduleticket.domain.ticket.event.listener;

import com.example.moduleticket.domain.reservation.entity.Reservation;
import com.example.moduleticket.domain.reservation.repository.ReservationRepository;
import com.example.moduleticket.domain.ticket.event.ReservationUnknownFailureEvent;
import com.example.moduleticket.global.exception.ErrorCode;
import com.example.moduleticket.global.exception.ServerException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReservationUnknownFailureEventListener {
	private final ReservationRepository reservationRepository;

	@EventListener
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void markUnknownFailed(ReservationUnknownFailureEvent reservationUnknownFailureEvent) {
		Reservation reservation = reservationRepository.findById(reservationUnknownFailureEvent.getReservationId())
			.orElseThrow(() -> new ServerException(ErrorCode.RESERVATION_NOT_FOUND));
		reservation.markUnknownFailed();
	}
}
