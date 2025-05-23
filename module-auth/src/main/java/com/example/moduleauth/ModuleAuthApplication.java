package com.example.moduleauth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
// test

@EnableJpaAuditing
@EnableDiscoveryClient
@SpringBootApplication
@EnableFeignClients("com.example.moduleauth.feign")
public class ModuleAuthApplication {

	public static void main(String[] args) {
		SpringApplication.run(ModuleAuthApplication.class, args);
	}

}
