package com.example.modulepoint.domain.point.controller;

import com.example.modulepoint.domain.point.dto.request.ExchangePointRequest;
import com.example.modulepoint.domain.point.dto.response.PointResponse;
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
}
