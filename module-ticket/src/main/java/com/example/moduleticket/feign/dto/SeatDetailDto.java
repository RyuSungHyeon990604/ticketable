package com.example.moduleticket.feign.dto;

import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class SeatDetailDto {
	private Long seatId;
	private long gameId;
	private long sectionId;
	private int gamePrice;
	private int sectionPrice;
}
