package com.example.moduleticket.domain.point.controller;

import com.example.moduleticket.domain.point.dto.response.PointHistoryResponse;
import com.example.moduleticket.domain.point.service.PointHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.web.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PointHistoryController {

	private final PointHistoryService pointHistoryService;
	
	@GetMapping("/v1/pointHistories")
	public ResponseEntity<PagedModel<PointHistoryResponse>> getPointHistories(
		//@AuthenticationPrincipal Auth auth,
		@RequestParam int page
	) {
		return ResponseEntity.ok(pointHistoryService.getPointHistories(auth.getId(), page));
	}
}
