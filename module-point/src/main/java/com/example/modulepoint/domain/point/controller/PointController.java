package com.example.modulepoint.domain.point.controller;

import com.example.modulepoint.domain.point.dto.request.ExchangePointRequest;
import com.example.modulepoint.domain.point.dto.request.PointPaymentRequestDto;
import com.example.modulepoint.domain.point.dto.response.PointResponse;
import com.example.modulepoint.domain.point.enums.PointHistoryType;
import com.example.modulepoint.domain.point.service.PointService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class PointController {

	private final PointService pointService;

	@PostMapping("/v1/members/{memberId}/points/exchange")
	public ResponseEntity<PointResponse> exchangePoint(
		@PathVariable Long memberId,
		@Valid @RequestBody ExchangePointRequest request
	) {
		return ResponseEntity.ok(pointService.exchangePoint(memberId, request));
	}

	@GetMapping("/v1/members/{memberId}/points")
	public ResponseEntity<PointResponse> getMemberPoint(
		@PathVariable Long memberId
	) {
		return ResponseEntity.ok(pointService.getMemberPoint(memberId));
	}
	
	@PostMapping("/v1/points")
	public ResponseEntity<Void> createPoint(
		@RequestParam Long memberId
		) {
		pointService.createPoint(memberId);
		return ResponseEntity.ok().build();
	}

	@PostMapping("/internal/members/{memberId}/points/increment")
	public ResponseEntity<Void> increasePoint(
		@PathVariable Long memberId,
		@RequestBody PointPaymentRequestDto pointPaymentRequestDto
	) {
		PointHistoryType type = PointHistoryType.valueOf(pointPaymentRequestDto.getType());
		pointService.increasePoint(
			memberId,
			pointPaymentRequestDto.getAmount(),
			type
		);
		return ResponseEntity.ok().build();
	}

	@PostMapping("/internal/members/{memberId}/points/decrement")
	public ResponseEntity<Void> decreasePoint(
		@PathVariable Long memberId,
		@RequestBody PointPaymentRequestDto pointPaymentRequestDto
	) {
		PointHistoryType type = PointHistoryType.valueOf(pointPaymentRequestDto.getType());
		pointService.decreasePoint(
			memberId,
			pointPaymentRequestDto.getAmount(),
			type
		);
		return ResponseEntity.ok().build();
	}

}
