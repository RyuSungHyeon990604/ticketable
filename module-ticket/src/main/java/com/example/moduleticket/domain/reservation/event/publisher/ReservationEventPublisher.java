package com.example.moduleticket.domain.reservation.event.publisher;

import com.example.moduleticket.domain.reservation.event.ReservationCancelledEvent;
import com.example.moduleticket.domain.reservation.event.ReservationPaymentComplete;
import com.example.moduleticket.domain.reservation.event.ReservationCreatedEvent;
import com.example.moduleticket.domain.reservation.event.ReservationExpiredEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReservationEventPublisher {
	private final ApplicationEventPublisher applicationEventPublisher;

	public void handleReservationCreated(ReservationCreatedEvent reservationCreatedEvent) {
		applicationEventPublisher.publishEvent(reservationCreatedEvent);
	}

	public void handleReservationCancelled(ReservationCancelledEvent reservationCancelledEvent) {
		applicationEventPublisher.publishEvent(reservationCancelledEvent);
	}

	public void handleReservationExpired(ReservationExpiredEvent reservationExpiredEvent) {
		applicationEventPublisher.publishEvent(reservationExpiredEvent);
	}

	public void handleReservationCompleted(ReservationPaymentComplete reservationCompleteEvent) {
		applicationEventPublisher.publishEvent(reservationCompleteEvent);
	}

}
