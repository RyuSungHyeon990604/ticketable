package com.example.moduleticket.feign;

import com.example.moduleticket.config.OpenFeignConfig;
import com.example.moduleticket.feign.dto.GameDto;
import com.example.moduleticket.feign.dto.SeatDetailDto;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "module-game", configuration = OpenFeignConfig.class)
public interface GameClient {
	@GetMapping("/api/internal/seats/by-section")
	List<SeatDetailDto> getSeatsByGameAndSection(
			@RequestParam Long gameId,
			@RequestParam Long sectionId,
			@RequestParam List<Long> seatIds
	);
}
