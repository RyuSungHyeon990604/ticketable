package com.example.moduleticket.domain.reservation.controller;

import com.example.moduleticket.domain.reservation.dto.ReservationCreateRequest;
import com.example.moduleticket.domain.reservation.service.ReservationService;
import com.example.moduleticket.domain.ticket.dto.response.TicketResponse;
import com.example.moduleticket.global.annotation.LoginUser;
import com.example.moduleticket.global.argumentresolver.AuthUser;
import com.example.moduleticket.global.dto.ApiResponse;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
	public ResponseEntity<ApiResponse<Void>> createReservation(
		@LoginUser AuthUser authUser,
		@RequestBody ReservationCreateRequest reservationCreateRequest
	) {
		return ResponseEntity.ok(reservationService.processReserve(authUser, reservationCreateRequest));
	}

	@PostMapping("/v1/reservations/{reservationId}")
	public ResponseEntity<TicketResponse> completePaymentReservation(
		@LoginUser AuthUser authUser,
		@PathVariable Long reservationId
	) {
		return ResponseEntity.ok(reservationService.processReservationCompletion(authUser, reservationId));
	}

	@GetMapping("/internal/reservations/games/{gameId}")
	public ResponseEntity<Set<Long>> getBookedSeatsId(@PathVariable Long gameId) {
		return ResponseEntity.ok(reservationService.getBookedSeatsId(gameId));
	}

	@PostMapping("/v1/reservations/{reservationId}/cancel")
	public ResponseEntity<String> cancelReservation(
		@LoginUser AuthUser authUser,
		@PathVariable Long reservationId
	) {
		reservationService.cancelReservation(authUser, reservationId);
		return ResponseEntity.ok("예약이 취소 되었습니다.");
	}
}
