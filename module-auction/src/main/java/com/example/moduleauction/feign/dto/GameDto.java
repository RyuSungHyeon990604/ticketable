package com.example.moduleauction.feign.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GameDto {
	private Long id;
	private String stadium;
	private String away;
	private String home;
	private String type;
	private Integer point;
	private LocalDateTime startTime;
	private LocalDateTime ticketingStartTime;
	private LocalDateTime deletedAt;

	public boolean isTimeOver() {
		return this.startTime.isBefore(LocalDateTime.now().plusHours(24));
	}
}
