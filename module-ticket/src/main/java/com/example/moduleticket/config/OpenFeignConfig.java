package com.example.moduleticket.config;

import com.example.moduleticket.feign.FeignClientErrorDecoder;
import feign.Retryer;
import feign.codec.ErrorDecoder;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients("com.example.moduleticket.feign")
public class OpenFeignConfig {
	@Bean
	public ErrorDecoder errorDecoder() {
		return new FeignClientErrorDecoder();
	}
}
