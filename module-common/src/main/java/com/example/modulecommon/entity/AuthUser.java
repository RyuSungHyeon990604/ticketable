package com.example.modulecommon.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class AuthUser {
	private final Long memberId;
	private final String role;
}
