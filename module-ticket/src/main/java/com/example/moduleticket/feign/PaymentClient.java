package com.example.moduleticket.feign;

import com.example.moduleticket.feign.dto.PaymentDto;
import com.example.moduleticket.feign.dto.request.PaymentRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "payment", url = "http://localhost:8081")
public interface PaymentClient {

	@PostMapping("/api/internal/decrement")
	PaymentDto decrement(
		@RequestHeader String uk,
		@RequestHeader Long memberId,
		@RequestBody PaymentRequest paymentRequest
	);
}
