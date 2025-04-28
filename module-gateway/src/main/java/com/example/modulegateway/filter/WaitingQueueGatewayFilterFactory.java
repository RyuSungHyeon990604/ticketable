package com.example.modulegateway.filter;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class WaitingQueueGatewayFilterFactory
	extends AbstractGatewayFilterFactory<WaitingQueueGatewayFilterFactory.Config> {

	private final WebClient webClient = WebClient.create("http://localhost:8085");
	private final AntPathMatcher pathMatcher = new AntPathMatcher();

	public WaitingQueueGatewayFilterFactory() {
		super(Config.class);
	}

	@Override
	public GatewayFilter apply(Config config) {
		return (exchange, chain) -> {
			Route route = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR);

			Boolean isWaiting = (Boolean) route.getMetadata().get("useWaiting");
			if(Boolean.FALSE.equals(isWaiting)) {
				return chain.filter(exchange);
			}

			String requestPath = exchange.getRequest().getPath().value();
			LinkedHashMap<String, String> applyPaths = (LinkedHashMap<String, String>) route.getMetadata().get("applyPath");
			if (applyPaths != null && applyPaths.values().stream().noneMatch(pattern -> pathMatcher.match(pattern, requestPath))) {
				return chain.filter(exchange); // 경로 제외 → 바로 통과
			}

			String token = exchange.getRequest().getHeaders().getFirst("waiting-token");
			return webClient.get()
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
				});
		};
	}

	public static class Config {

	}
}
