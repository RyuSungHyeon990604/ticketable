package com.example.modulegateway.config;

import io.netty.channel.ChannelOption;
import java.time.Duration;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

@Configuration
public class WebClientConfig {



	@Bean
	@LoadBalanced // Eureka 연동을 위해 반드시 필요!
	public WebClient.Builder loadBalancedWebClientBuilder() {
		return WebClient.builder();
	}


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
	public WebClient waitingWebClient(@Qualifier("loadBalancedWebClientBuilder")WebClient.Builder builder) {
		HttpClient httpClient = HttpClient.create(connectionProvider())
			.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)// 연결 타임아웃 5초
			.responseTimeout(Duration.ofSeconds(5));// 응답 타임아웃 5초
		return builder
			.baseUrl("http://module-waiting")
			.clientConnector(new ReactorClientHttpConnector(httpClient))
			.build();
	}

	@Bean
	public WebClient authWebClient(@Qualifier("loadBalancedWebClientBuilder")WebClient.Builder builder) {
		HttpClient httpClient = HttpClient.create(connectionProvider())
			.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)// 연결 타임아웃 5초
			.responseTimeout(Duration.ofSeconds(5));// 응답 타임아웃 5초
		return builder
			.baseUrl("http://module-auth")
			.clientConnector(new ReactorClientHttpConnector(httpClient))
			.build();
	}
}
