package com.example.moduleticket.feign;

import com.example.moduleticket.feign.dto.GameDto;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "game", url = "http://localhost:8081")
public interface GameService {

	@GetMapping("/api/internal/games/{gameId}")
	public GameDto getGame(@PathVariable Long gameId);

	@GetMapping("/api/internal/games")
	public List<GameDto> getGames(@RequestBody List<Long> gameIds);
}
