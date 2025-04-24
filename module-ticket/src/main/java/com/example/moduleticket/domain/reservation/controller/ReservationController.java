package com.example.moduleticket.domain.reservation.controller;

import com.example.modulecommon.annotation.LoginUser;
import com.example.modulecommon.entity.AuthUser;
import com.example.moduleticket.domain.reservation.dto.ReservationCreateRequest;
import com.example.moduleticket.domain.reservation.dto.ReservationResponse;
import com.example.moduleticket.domain.reservation.service.ReservationService;
import com.example.moduleticket.domain.ticket.dto.response.TicketResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ReservationController {

	private final ReservationService reservationService;

	@PostMapping("/v1/reservations")
	public ResponseEntity<ReservationResponse> createReservation(
		@LoginUser AuthUser authUser,
		@RequestBody ReservationCreateRequest reservationCreateRequest
	) {
		return ResponseEntity.ok(reservationService.createReservation(authUser, reservationCreateRequest));
	}

	@PostMapping("/v1/reservations/{reservationId}")
	public ResponseEntity<TicketResponse> completePaymentReservation(
		@LoginUser AuthUser authUser,
		@PathVariable Long reservationId
	) {
		return ResponseEntity.ok(reservationService.processReservationCompletion(authUser, reservationId));
	}
}
