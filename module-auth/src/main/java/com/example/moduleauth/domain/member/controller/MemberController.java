package com.example.moduleauth.domain.member.controller;

import com.example.moduleauth.domain.member.dto.request.DeleteMemberRequest;
import com.example.moduleauth.domain.member.dto.request.UpdatePasswordRequest;
import com.example.moduleauth.domain.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class MemberController {
	
	private final MemberService memberService;
	
	@PatchMapping("/v1/members")
	public ResponseEntity<String> updatePassword(
		//@AuthenticationPrincipal Auth auth,
		@Valid @RequestBody UpdatePasswordRequest request
	) {
		memberService.updatePassword(auth.getId(), request);
		return ResponseEntity.ok("비밀번호가 변경되었습니다.");
	}
	
	@DeleteMapping("/v1/members")
	public ResponseEntity<String> deleteMember(
		//@AuthenticationPrincipal Auth auth,
		@Valid @RequestBody DeleteMemberRequest request
	) {
		memberService.deleteMember(auth.getId(), request);
		return ResponseEntity.ok("멤버가 삭제되었습니다.");
	}
}
