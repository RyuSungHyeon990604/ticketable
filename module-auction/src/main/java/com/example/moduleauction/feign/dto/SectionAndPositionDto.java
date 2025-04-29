package com.example.moduleauction.feign.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SectionAndPositionDto {
	private String type;
	private String code;
	private List<String> positions;
}
