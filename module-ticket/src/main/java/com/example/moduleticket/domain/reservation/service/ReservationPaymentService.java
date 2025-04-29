package com.example.moduleticket.domain.reservation.service;

import com.example.modulecommon.entity.AuthUser;
import com.example.modulecommon.exception.ServerException;
import com.example.moduleticket.domain.ticket.event.ReservationUnknownFailureEvent;
import com.example.moduleticket.feign.PaymentClient;
import com.example.moduleticket.feign.dto.request.PointPaymentRequestDto;
import com.example.moduleticket.util.IdempotencyKeyUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReservationPaymentService {

	private final PaymentClient paymentClient;
	private final ApplicationEventPublisher eventPublisher;

	public void reservePayment(AuthUser authUser, Long reservationId, int price) {

		PointPaymentRequestDto requestDto = new PointPaymentRequestDto(
			IdempotencyKeyUtil.forReservation(reservationId, "decrement"),
			"RESERVATION",
			price,
			authUser.getMemberId()
		);

		//결제요청
		try {
			paymentClient.processPayment(
				authUser.getMemberId(),
				requestDto
			);
		} catch (ServerException e) {
			throw e;
		} catch (Exception e) {
			//알수없는 오류이므로 다시결제요청
			reTryPayment(
				requestDto,
				authUser.getMemberId(),
				reservationId
			);
		}
	}

	private void reTryPayment(PointPaymentRequestDto request, Long memberId, Long reservationId) {
		try {
			paymentClient.processPayment(memberId, request);
		} catch (ServerException e) {
			throw e;
		} catch (Exception e) {
			//알수없는 예외로 마킹
			eventPublisher.publishEvent(new ReservationUnknownFailureEvent(reservationId));
			throw e;
		}
	}
}
