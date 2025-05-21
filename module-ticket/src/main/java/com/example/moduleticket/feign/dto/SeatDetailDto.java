package com.example.moduleticket.feign.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SeatDetailDto {
	private Long seatId;
	private long gameId;
	private long sectionId;
	private int gamePrice;
	private int sectionPrice;
}
