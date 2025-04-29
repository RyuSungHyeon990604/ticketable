package com.example.modulegateway.filter;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class WaitingQueueGatewayFilterFactory
	extends AbstractGatewayFilterFactory<WaitingQueueGatewayFilterFactory.Config> {

	private final WebClient waitingWebClient;

	public WaitingQueueGatewayFilterFactory(@Qualifier("waitingWebClient") WebClient waitingWebClient) {
		super(Config.class);
		this.waitingWebClient = waitingWebClient;
	}

	@Override
	public GatewayFilter apply(Config config) {
		return (exchange, chain) -> {
			String token = exchange.getRequest().getHeaders().getFirst("waiting-token");
			return waitingWebClient.get()
				.uri("/api/v1/waiting-queue/order")
				.header("waiting-token", token != null ? token : "")
				.retrieve()
				.bodyToMono(JsonNode.class)
				.flatMap(json -> {
					String state = json.path("state").asText();

					if ("allow".equalsIgnoreCase(state)) {
						return chain.filter(exchange);
					} else {
						String body = json.toString();
						exchange.getResponse().getHeaders().add(HttpHeaders.CONTENT_TYPE, "application/json");
						exchange.getResponse().setStatusCode(HttpStatus.ACCEPTED);
						return exchange.getResponse().writeWith(Mono.just(
							exchange.getResponse().bufferFactory().wrap(body.getBytes())
						));
					}
				}).then();
		};
	}

	public static class Config {

	}
}
