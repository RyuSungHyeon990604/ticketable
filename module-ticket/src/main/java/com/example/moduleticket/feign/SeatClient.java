package com.example.moduleticket.feign;

import com.example.moduleticket.config.OpenFeignConfig;
import com.example.moduleticket.feign.dto.SeatDetailDto;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "seat", url = "http://localhost:8081", configuration = OpenFeignConfig.class)
public interface SeatClient {

	@GetMapping("/api/internal/seats/by-section")
	List<SeatDetailDto> getSeatsByGameAndSection(
		@RequestParam Long gameId,
		@RequestParam Long sectionId,
		@RequestParam List<Long> seatIds
	);

	@GetMapping("/api/internal/seats/by-game")
	List<SeatDetailDto> getSeatsByGame(
		@RequestParam Long gameId,
		@RequestParam List<Long> seatIds
	);
}
