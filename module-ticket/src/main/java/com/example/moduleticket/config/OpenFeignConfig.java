package com.example.moduleticket.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients("com.example.moduleticket.feign")
public class OpenFeignConfig {

}
