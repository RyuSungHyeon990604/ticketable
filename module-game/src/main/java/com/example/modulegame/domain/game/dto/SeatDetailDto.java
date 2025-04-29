package com.example.modulegame.domain.game.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
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
