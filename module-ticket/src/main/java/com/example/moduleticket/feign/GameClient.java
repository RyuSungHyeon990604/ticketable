package com.example.moduleticket.feign;

import com.example.moduleticket.config.OpenFeignConfig;
import com.example.moduleticket.feign.dto.GameDto;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "game", url = "http://localhost:8081", configuration = OpenFeignConfig.class)
public interface GameClient {

	@GetMapping("/api/internal/games/{gameId}")
	public GameDto getGame(@PathVariable Long gameId);

	@GetMapping("/api/internal/games")
	public List<GameDto> getGames(@RequestParam List<Long> gameIds);
}
