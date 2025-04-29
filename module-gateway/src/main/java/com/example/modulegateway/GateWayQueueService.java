package com.example.modulegateway;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.cloud.gateway.route.RouteDefinitionWriter;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class GateWayQueueService {
	private static final String WAITING_ROUTE = "waitingRoute";
	private final RouteDefinitionWriter routeDefinitionWriter;
	private final RouteDefinitionLocator routeDefinitionLocator;
	private final ApplicationEventPublisher publisher;

	public Mono<Void> applyQueue() {
		return routeDefinitionLocator.getRouteDefinitions()
			.filter(routeDefinition -> routeDefinition.getId().equals(WAITING_ROUTE))
			.hasElements()
			.flatMap(exists -> {
				if(exists) {
					return Mono.empty();
				}
				return routeDefinitionLocator.getRouteDefinitions()
					.filter(route -> route.getId().equals("module-game"))
					.next()
					.flatMap(route -> {
						RouteDefinition routeDefinition = new RouteDefinition();
						routeDefinition.setId(WAITING_ROUTE);
						routeDefinition.setUri(URI.create(route.getUri().toString()));
						routeDefinition.setOrder(-1);

						// Predicate 생성 (Path 매칭)
						PredicateDefinition pathPredicate = new PredicateDefinition();
						pathPredicate.setName("Path");
						pathPredicate.setArgs(new HashMap<>(Map.of(
							"pattern", "/api/v*/games/**"
						)));
						routeDefinition.setPredicates(List.of(pathPredicate));

						// Filters 생성
						FilterDefinition waitingQueueFilter = new FilterDefinition();
						waitingQueueFilter.setName("WaitingQueue");
						waitingQueueFilter.setArgs(new HashMap<>());

						FilterDefinition rewritePathFilter = new FilterDefinition();
						rewritePathFilter.setName("RewritePath");
						rewritePathFilter.setArgs(new HashMap<>(Map.of(
							"regexp", "/" + "module-game" + "/(?<segment>.*)",
							"replacement", "/${segment}"
						)));

						routeDefinition.setFilters(List.of(waitingQueueFilter, rewritePathFilter));

						return routeDefinitionWriter.save(Mono.just(routeDefinition)).doOnSuccess(
							a -> publisher.publishEvent(new RefreshRoutesEvent(this))
						);
					});
			});
	}

	public Mono<Void> deactivateQueue() {
		return routeDefinitionLocator.getRouteDefinitions()
			.filter(routeDefinition -> routeDefinition.getId().equals(WAITING_ROUTE))
			.hasElements()
			.flatMap(exists->{
				if(exists) {
					return routeDefinitionWriter.delete(Mono.just(WAITING_ROUTE))
						.doOnSuccess(unused -> publisher.publishEvent(new RefreshRoutesEvent(this)));
				}
				return Mono.empty();
			});
	}
}
