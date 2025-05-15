package com.example.moduleticket.feign;

import com.example.moduleticket.feign.dto.PaymentDto;
import com.example.moduleticket.feign.dto.request.PointPaymentRequestDto;
import com.example.moduleticket.global.exception.ServerException;
import com.example.moduleticket.global.exception.UnknownException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PointClientFallbackFactory implements FallbackFactory<PointClient> {
	@Override
	public PointClient create(Throwable cause) {
		return new PointClient() {

			@Override
			public PaymentDto processPayment(Long memberId, PointPaymentRequestDto pointPaymentRequestDto) {
				if(cause instanceof ServerException) {
					throw (ServerException) cause;
				}
				throw new UnknownException("Feign 호출 중 알 수 없는 오류가 발생했습니다. : " + cause.getMessage());
			}

			@Override
			public PaymentDto processRefund(Long memberId, PointPaymentRequestDto pointPaymentRequestDto) {
				if(cause instanceof ServerException) {
					throw (ServerException) cause;
				}
				throw new UnknownException("Feign 호출 중 알 수 없는 오류가 발생했습니다. : " + cause.getMessage());

			}
		};
	}
}
