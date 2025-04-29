package com.example.modulegateway.config;

import io.netty.channel.ChannelOption;
import java.time.Duration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

@Configuration
public class WebClientConfig {

	@Bean
	public ConnectionProvider connectionProvider() {
		return ConnectionProvider.builder("custom")
			.maxConnections(500)
			.pendingAcquireMaxCount(1000)
			.maxIdleTime(Duration.ofSeconds(30))
			.maxLifeTime(Duration.ofMinutes(2))
			.metrics(true)
			.build();
	}

	@Bean
	public WebClient waitingWebClient() {
		HttpClient httpClient = HttpClient.create(connectionProvider())
			.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)// 연결 타임아웃 5초
			.responseTimeout(Duration.ofSeconds(5));// 응답 타임아웃 5초
		return WebClient.builder()
			.baseUrl("http://localhost:8085")
			.clientConnector(new ReactorClientHttpConnector(httpClient))
			.build();
	}
}
