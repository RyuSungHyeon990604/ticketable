package com.example.moduleticket.feign;

import com.example.moduleticket.feign.dto.SeatDto;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "seat", url = "http://localhost:8081")
public interface SeatService {
	@GetMapping("/api/internal/seats")
	List<SeatDto> getSeats(@RequestBody List<Long> seatIds);
}
