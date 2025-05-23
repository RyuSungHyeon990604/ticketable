package com.example.moduleticket.feign;

import com.example.moduleticket.config.OpenFeignConfig;
import com.example.moduleticket.feign.dto.PaymentDto;
import com.example.moduleticket.feign.dto.request.PointPaymentRequestDto;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "module-point", configuration = OpenFeignConfig.class, fallbackFactory = PointClientFallbackFactory.class)
public interface PointClient {

	@PostMapping("/api/v2/internal/members/{memberId}/points/decrement")
	PaymentDto processPayment(
		@PathVariable Long memberId,
		@RequestBody PointPaymentRequestDto pointPaymentRequestDto
	);

	@PostMapping("/api/v2/internal/members/{memberId}/points/increment")
	PaymentDto processRefund(
		@PathVariable Long memberId,
		@RequestBody PointPaymentRequestDto pointPaymentRequestDto
	);
}
