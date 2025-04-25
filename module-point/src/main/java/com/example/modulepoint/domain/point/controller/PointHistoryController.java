package com.example.modulepoint.domain.point.controller;

import com.example.modulepoint.domain.point.dto.response.PointHistoryResponse;
import com.example.modulepoint.domain.point.service.PointHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.web.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PointHistoryController {

	private final PointHistoryService pointHistoryService;

	@GetMapping("/v1/members/{memberId}/pointHistories")
	public ResponseEntity<PagedModel<PointHistoryResponse>> getPointHistories(
		@PathVariable Long memberId,
		@RequestParam int page
	) {
		return ResponseEntity.ok(pointHistoryService.getPointHistories(memberId, page));
	}
}
