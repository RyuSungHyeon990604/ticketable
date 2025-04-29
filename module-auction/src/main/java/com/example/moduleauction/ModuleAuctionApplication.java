package com.example.moduleauction;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class ModuleAuctionApplication {

	public static void main(String[] args) {
		SpringApplication.run(ModuleAuctionApplication.class, args);
	}

}
