package com.example.moduleauth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableFeignClients("com.example.moduleauth.feign")
@EnableJpaRepositories("com.example.modulemember.member.repository")
@EntityScan("com.example.modulemember.member.entity")
public class ModuleAuthApplication {

	public static void main(String[] args) {
		SpringApplication.run(ModuleAuthApplication.class, args);
	}

}
