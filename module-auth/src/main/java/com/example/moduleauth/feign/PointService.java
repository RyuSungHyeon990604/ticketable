package com.example.moduleauth.feign;

import com.example.modulepoint.domain.point.dto.request.CreatePointRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "point", url = "http://localhost:8087")
public interface PointService {

	@PostMapping("/api/v1/points")
	void createPoint(@RequestBody CreatePointRequest request);
}
