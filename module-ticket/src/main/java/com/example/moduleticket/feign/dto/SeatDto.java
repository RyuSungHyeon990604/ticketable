package com.example.moduleticket.feign.dto;

import lombok.Getter;

@Getter
public class SeatDto {
	private Long seatId;
	private String position;
	private int SectionExtraCharge;
	private int price;
}
