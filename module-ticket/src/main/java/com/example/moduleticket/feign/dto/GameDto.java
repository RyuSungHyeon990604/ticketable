package com.example.moduleticket.feign.dto;

import java.time.LocalDateTime;
import lombok.Getter;

@Getter
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
}
