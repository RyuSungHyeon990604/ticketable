package com.example.modulegateway.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RouteCreateRequest {
	private String id;
	private String pathPattern;
	private String applyModule;

}
