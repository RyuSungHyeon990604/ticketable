package com.example.moduleticket.domain.point.controller;

import com.example.moduleticket.domain.point.dto.response.PointExchangeResponse;
import com.example.moduleticket.domain.point.dto.response.PointHistoryResponse;
import com.example.moduleticket.domain.point.service.PointExchangeService;
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
		////@AuthenticationPrincipal Auth auth,
		@PathVariable Long pointHistoryId
	) {
		return ResponseEntity.ok(pointExchangeService.exchangePoint(auth, pointHistoryId));
	}
	
	@GetMapping("/v1/admin/pointHistory/{pointHistoryId}")
	public ResponseEntity<PointHistoryResponse> getExchangeRequestPointHistory(
		////@AuthenticationPrincipal Auth auth,
		@PathVariable Long pointHistoryId
	) {
		return ResponseEntity.ok(pointExchangeService.getExchangeRequestPointHistory(auth, pointHistoryId));
	}
	
	@GetMapping("/v1/admin/pointHistory")
	public ResponseEntity<PagedModel<PointHistoryResponse>> getExchangeRequestPointHistories(
		//@AuthenticationPrincipal Auth auth,
		@RequestParam int page
	) {
		return ResponseEntity.ok(pointExchangeService.getExchangeRequestPointHistories(auth, page));
	}
}
