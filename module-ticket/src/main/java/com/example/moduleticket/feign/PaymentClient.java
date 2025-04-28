package com.example.moduleticket.feign;

import com.example.moduleticket.domain.ticket.dto.RefundDto;
import com.example.moduleticket.feign.dto.PaymentDto;
import com.example.moduleticket.feign.dto.request.PaymentRequest;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "payment", url = "http://localhost:8081")
public interface PaymentClient {

	@PostMapping("/api/internal/payments")
	PaymentDto processPayment(
		@RequestHeader String idempotencyKey,
		@RequestHeader Long memberId,
		@RequestBody PaymentRequest paymentRequest
	);

	@PostMapping("/api/internal/refund")
	PaymentDto processRefund(
		@RequestBody RefundDto refundDto
	);

	@PostMapping("/api/internal/refund-bulk")
	PaymentDto processRefundBulk(
		@RequestBody List<RefundDto> refundDtoList
	);

}
