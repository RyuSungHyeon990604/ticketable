package com.example.modulecommon.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
// 테스트용 주석
@Getter
@RequiredArgsConstructor
public class AuthUser {
	private final Long memberId;
	private final String role;
}
