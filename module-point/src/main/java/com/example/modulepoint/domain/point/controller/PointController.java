package com.example.modulepoint.domain.point.controller;

import com.example.modulecommon.annotation.LoginUser;
import com.example.modulecommon.entity.AuthUser;
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

	@PostMapping("/v1/points/exchange")
	public ResponseEntity<PointResponse> exchangePoint(
		@LoginUser AuthUser authUser,
		@Valid @RequestBody ExchangePointRequest request
	) {
		return ResponseEntity.ok(pointService.exchangePoint(authUser.getMemberId(), request));
	}

	@GetMapping("/v1/points")
	public ResponseEntity<PointResponse> getMemberPoint(
		@LoginUser AuthUser authUser
		) {
		return ResponseEntity.ok(pointService.getMemberPoint(authUser.getMemberId()));
	}
	
	@PostMapping("/v1/points")
	public ResponseEntity<Void> createPoint(
		@RequestParam Long memberId
	) {
		pointService.createPoint(memberId);
		return ResponseEntity.ok().build();
	}
}
