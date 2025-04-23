//package com.example.moduleticket.domain.point.controller;
//
//import com.example.moduleticket.domain.point.dto.request.ExchangePointRequest;
//import com.example.moduleticket.domain.point.dto.response.PointResponse;
//import com.example.moduleticket.domain.point.service.PointService;
//import jakarta.validation.Valid;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RequiredArgsConstructor
//@RestController
//@RequestMapping("/api")
//public class PointController {
//
//	private final PointService pointService;
//
//	@PostMapping("/v1/points/exchange")
//	public ResponseEntity<PointResponse> exchangePoint(
//		//@AuthenticationPrincipal Auth auth,
//		@Valid @RequestBody ExchangePointRequest request
//	) {
//		return ResponseEntity.ok(pointService.exchangePoint(auth.getId(), request));
//	}
//
//	@GetMapping("/v1/points")
//	public ResponseEntity<PointResponse> getMemberPoint(
//		//@AuthenticationPrincipal Auth auth
//	) {
//		return ResponseEntity.ok(pointService.getMemberPoint(auth.getId()));
//	}
//}
