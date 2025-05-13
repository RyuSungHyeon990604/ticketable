package com.example.moduleticket.global.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T>{
	private final T data;
	private final String message;
	private final LocalDateTime timestamp;
	public ApiResponse(T data, String message) {
		this.data = data;
		this.message = message;
		this.timestamp = LocalDateTime.now();
	}

	public static ApiResponse<Void> messageOnly(String message) {
		return new ApiResponse<>(null, message);
	}

	public static <T> ApiResponse<T> of(T data, String message) {
		return new ApiResponse<>(data, message);
	}
}
