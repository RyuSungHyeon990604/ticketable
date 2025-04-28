package com.example.moduleauction.feign;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.moduleauction.feign.dto.SectionAndPositionDto;

@FeignClient(name = "seat", url = "http://localhost:8081")
public interface SeatClient {

	@GetMapping("/internal/sections/seats")
	SectionAndPositionDto getSectionAndPositions(List<Long> seatIds);
}
