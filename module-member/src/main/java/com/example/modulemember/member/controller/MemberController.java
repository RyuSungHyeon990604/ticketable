package com.example.modulemember.member.controller;

import com.example.modulemember.member.dto.request.DeleteMemberRequest;
import com.example.modulemember.member.dto.request.UpdatePasswordRequest;
import com.example.modulemember.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class MemberController {
	
	private final MemberService memberService;
	
	@PatchMapping("/v1/members/{memberId}")
	public ResponseEntity<String> updatePassword(
		@PathVariable Long memberId,
		@Valid @RequestBody UpdatePasswordRequest request
	) {
		memberService.updatePassword(memberId, request);
		return ResponseEntity.ok("비밀번호가 변경되었습니다.");
	}
	
	@DeleteMapping("/v1/members/{memberId}")
	public ResponseEntity<String> deleteMember(
		@PathVariable Long memberId,
		@Valid @RequestBody DeleteMemberRequest request
	) {
		memberService.deleteMember(memberId, request);
		return ResponseEntity.ok("멤버가 삭제되었습니다.");
	}
}
