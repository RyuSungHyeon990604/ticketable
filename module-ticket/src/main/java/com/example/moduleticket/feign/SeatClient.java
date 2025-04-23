package com.example.moduleticket.feign;

import com.example.moduleticket.feign.dto.SeatDto;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "seat", url = "http://localhost:8081")
public interface SeatClient {

	@GetMapping("/api/internal/seats")
	List<SeatDto> getSeats(@RequestParam Long gameId, @RequestParam List<Long> seatIds);
}
