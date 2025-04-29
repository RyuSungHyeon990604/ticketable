package com.example.moduleauction.feign.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TicketDto {

	private Long id;
	private Long memberId;
	private Long gameId;
	private LocalDateTime deletedAt;
	private Integer totalPoint;
	private List<Long> seatIds;
}