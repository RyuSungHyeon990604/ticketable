package com.example.moduleauction.feign;

import com.example.moduleauction.feign.dto.SectionAndPositionDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.moduleauction.feign.dto.GameDto;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "module-game")
public interface GameClient {

	@GetMapping("/api/internal/games/{gameId}")
	GameDto getGame(@PathVariable("gameId") Long gameId);

	@PostMapping("/api/internal/sections/seats")
	SectionAndPositionDto getSectionAndPositions(@RequestBody List<Long> seatIds);
}
