package com.example.moduleauth.domain.auth.controller;

import com.example.moduleauth.domain.auth.dto.request.LoginRequest;
import com.example.moduleauth.domain.auth.dto.request.SignupRequest;
import com.example.moduleauth.domain.auth.dto.response.AuthResponse;
import com.example.moduleauth.domain.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class AuthController {
	
	private final AuthService authService;
	
	@PostMapping("/v1/auth/signup")
	public ResponseEntity<AuthResponse> signup(@Valid @RequestBody SignupRequest request) {
		return ResponseEntity.ok(authService.signup(request));
	}
	
	@PostMapping("/v1/auth/login")
	public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
		return ResponseEntity.ok(authService.login(request));
	}
	
	@PostMapping("/v1/auth/validate")
	public ResponseEntity<Void> validateToken(
		@RequestHeader("Authorization") String authToken,
		@RequestParam(required = false) String requiredRole
	) {
		log.info("토큰 검증 진입 성공");
		log.info("requiredRole : {}", requiredRole);
		authService.validateToken(authToken, requiredRole);
		return ResponseEntity.ok().build();
	}
}
