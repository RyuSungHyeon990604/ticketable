package com.example.moduleauth.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "module-point")
public interface PointService {

	@PostMapping("/api/v1/points")
	void createPoint(@RequestParam Long memberId);
}
