package com.example.moduleticket.domain.queue.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class WaitingResponse {
	private final long order;
	private final String state;
	private final String token;
	private final long expectedWaitingSec;
	private final long pollingSec;
}
