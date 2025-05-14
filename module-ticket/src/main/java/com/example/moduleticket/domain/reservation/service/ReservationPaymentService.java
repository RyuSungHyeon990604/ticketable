package com.example.moduleticket.domain.reservation.service;

import com.example.moduleticket.domain.ticket.event.ReservationUnknownFailureEvent;
import com.example.moduleticket.feign.PointClient;
import com.example.moduleticket.feign.dto.request.PointPaymentRequestDto;
import com.example.moduleticket.global.argumentresolver.AuthUser;
import com.example.moduleticket.global.exception.ServerException;
import com.example.moduleticket.util.IdempotencyKeyUtil;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationPaymentService {

	private final PointClient pointClient;
	private final ApplicationEventPublisher eventPublisher;

	public void reservePayment(AuthUser authUser, Long reservationId, int price, List<Long> seatIds, Long gameId) {

		PointPaymentRequestDto requestDto = new PointPaymentRequestDto(
			IdempotencyKeyUtil.forReservation(reservationId, "decrement"),
			"RESERVATION",
			price,
			authUser.getMemberId()
		);

		//결제요청
		try {
			pointClient.processPayment(
				authUser.getMemberId(),
				requestDto
			);
		} catch (ServerException e) {
			throw e;
		} catch (Exception e) {
			log.error("결제 재시도까지 실패: memberId={}, reservationId={}, err={}",
				authUser.getMemberId(), reservationId, e.getMessage(), e);
			eventPublisher.publishEvent(new ReservationUnknownFailureEvent(reservationId, seatIds, gameId, authUser.getMemberId()));
			throw e;
		}
	}
}
