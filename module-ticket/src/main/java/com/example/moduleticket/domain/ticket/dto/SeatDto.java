package com.example.moduleticket.domain.ticket.dto;

import lombok.Getter;

@Getter
public class SeatDto {
	private Long seatId;
	private String position;
	private int SectionExtraCharge;
}
