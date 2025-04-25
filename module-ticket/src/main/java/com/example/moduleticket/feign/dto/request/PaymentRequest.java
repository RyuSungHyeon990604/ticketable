package com.example.moduleticket.feign.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PaymentRequest {
	private int amount;
	private String action;
	//reservation, auction
	private String type;
	private Long typeIdentity;
}
