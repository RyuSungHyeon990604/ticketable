package com.example.moduleauction.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.moduleauction.feign.dto.GameDto;

@FeignClient(name = "game", url = "http://localhost:8081")
public interface GameClient {

	@GetMapping("/api/internal/games/{gameId}")
	GameDto getGame(@PathVariable Long gameId);
}
