package com.example.moduleticket.feign.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PaymentRequest {
	private int amount;
	//reservation
	private String type;
	private Long typeIdentity;
}
