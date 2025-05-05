package com.example.modulepoint;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

//포인트 재배포
@EnableDiscoveryClient
@SpringBootApplication
public class ModulePointApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(ModulePointApplication.class, args);
	}
	
}
