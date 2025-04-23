//package com.example.moduleticket.domain.point.controller;
//
//import com.example.moduleticket.domain.point.service.PointPaymentService;
//import com.siot.IamportRestClient.response.IamportResponse;
//import com.siot.IamportRestClient.response.Payment;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RequiredArgsConstructor
//@RestController
//@RequestMapping("/api")
//public class PointPaymentController {
//
//	private final PointPaymentService pointPaymentService;
//
//	@PostMapping("/v1/payments/{imp_uid}")
//	public ResponseEntity<IamportResponse<Payment>> iamPortPayment(
//		//@AuthenticationPrincipal Auth auth,
//		@PathVariable String imp_uid
//	) {
//		return ResponseEntity.ok(pointPaymentService.iamPortPayment(auth, imp_uid));
//	}
//}
