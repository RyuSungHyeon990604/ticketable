package com.example.modulepoint.domain.point.controller;

import com.example.modulepoint.domain.point.dto.response.PointExchangeResponse;
import com.example.modulepoint.domain.point.dto.response.PointHistoryResponse;
import com.example.modulepoint.domain.point.service.PointExchangeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.web.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class PointExchangeController {

	private final PointExchangeService pointExchangeService;

	@PatchMapping("/v1/admin/pointHistory/{pointHistoryId}/exchange")
	public ResponseEntity<PointExchangeResponse> exchangePoint(
		@PathVariable Long pointHistoryId
	) {
		return ResponseEntity.ok(pointExchangeService.exchangePoint(pointHistoryId));
	}

	@GetMapping("/v1/admin/pointHistory/{pointHistoryId}")
	public ResponseEntity<PointHistoryResponse> getExchangeRequestPointHistory(
		@PathVariable Long pointHistoryId
	) {
		return ResponseEntity.ok(pointExchangeService.getExchangeRequestPointHistory(pointHistoryId));
	}

	@GetMapping("/v1/admin/pointHistory")
	public ResponseEntity<PagedModel<PointHistoryResponse>> getExchangeRequestPointHistories(
		@RequestParam int page
	) {
		return ResponseEntity.ok(pointExchangeService.getExchangeRequestPointHistories(page));
	}
}
