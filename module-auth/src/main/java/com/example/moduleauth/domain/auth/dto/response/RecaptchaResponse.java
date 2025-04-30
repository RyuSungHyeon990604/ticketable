package com.example.moduleauth.domain.auth.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class RecaptchaResponse {
	private final boolean success;
	@JsonProperty("error-codes")
	private List<String> errorCodes;
}