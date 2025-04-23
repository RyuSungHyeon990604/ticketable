package com.example.moduleauth.domain.auth.service;

import com.example.moduleauth.domain.auth.dto.request.LoginRequest;
import com.example.moduleauth.domain.auth.dto.request.SignupRequest;
import com.example.moduleauth.domain.auth.dto.response.AuthResponse;
import com.example.moduleauth.config.PasswordEncoder;
import com.example.moduleauth.domain.member.entity.Member;
import com.example.modulecommon.exception.ServerException;
import com.example.modulegateway.role.MemberRole;
import com.example.modulegateway.util.JwtUtil;
import com.example.moduleauth.domain.member.repository.MemberRepository;
import com.example.moduleticket.domain.point.service.PointService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.modulecommon.exception.ErrorCode.*;

@RequiredArgsConstructor
@Service
public class AuthService {
	
	private final MemberRepository memberRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtUtil jwtUtil;
	private final PointService pointService;
	
	@Transactional
	public AuthResponse signup(SignupRequest request) {
		if (!request.validRePassword()) {
			throw new ServerException(INVALID_PASSWORD);
		}
		
		if (memberRepository.existsByEmail(request.getEmail())) {
			throw new ServerException(USER_EMAIL_DUPLICATION);
		}
		
		Member member = Member.builder()
			.email(request.getEmail())
			.name(request.getName())
			.password(passwordEncoder.encode(request.getPassword()))
			.role(MemberRole.of(request.getRole()))
			.build();
		Member savedMember = memberRepository.save(member);

		pointService.createPoint(savedMember);

		String accessToken = jwtUtil.createAccessToken(
			savedMember.getId(), savedMember.getEmail(), savedMember.getName(), savedMember.getRole()
		);
		return new AuthResponse(accessToken);
	}
	
	@Transactional
	public AuthResponse login(LoginRequest request) {
		Member findMember = memberRepository.findByEmail(request.getEmail())
			.orElseThrow(() -> new ServerException(USER_NOT_FOUND));
		
		if (!passwordEncoder.matches(request.getPassword(), findMember.getPassword())) {
			throw new ServerException(INVALID_PASSWORD);
		}
		
		String accessToken = jwtUtil.createAccessToken(
			findMember.getId(), findMember.getEmail(), findMember.getName(), findMember.getRole()
		);
		return new AuthResponse(accessToken);
	}
}
