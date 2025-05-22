package com.example.moduleticket.config;

import java.util.concurrent.Executor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@EnableAsync
@Configuration
public class AsyncConfig {

	@Bean("refreshCacheThreadPool")
	public Executor asyncExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(1);  // 최소 스레드 수
		executor.setMaxPoolSize(5);   // 최대 스레드 수
		executor.setQueueCapacity(1000); // 큐에 대기 가능한 작업 수
		executor.setThreadNamePrefix("refresh-cache-thread-");
		executor.initialize();
		return executor;
	}
}
