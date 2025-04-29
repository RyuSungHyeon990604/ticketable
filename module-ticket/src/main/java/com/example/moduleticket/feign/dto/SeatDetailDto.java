package com.example.moduleticket.feign.dto;

import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class SeatDetailDto {
	private Long seatId;
	private String position;

	private long gameId;
	private LocalDateTime startTime;
	private String home;
	private String away;

	private long sectionId;
	private String sectionType;

	private int gamePrice;
	private int sectionPrice;
}
