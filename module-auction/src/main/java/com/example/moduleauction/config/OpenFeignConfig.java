package com.example.moduleauction.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients("com.example.moduleauction.feign")
public class OpenFeignConfig {

}
