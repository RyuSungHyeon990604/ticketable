package com.example.moduleauction.domain.auction.service;

import org.springframework.stereotype.Service;

import com.example.moduleauction.feign.PointClient;
import com.example.moduleauction.feign.dto.request.PointPaymentRequestDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuctionPaymentService {

	private final PointClient pointClient;

	public void processPayment(Long memberId, int price, String type) {
		PointPaymentRequestDto pointPaymentRequestDto = new PointPaymentRequestDto(
			memberId + type, type, price, memberId);
		pointClient.processPayment(memberId, pointPaymentRequestDto);
	}

	public void processRefund(Long memberId, int price, String type) {
		PointPaymentRequestDto pointPaymentRequestDto = new PointPaymentRequestDto(
			memberId + type,type, price, memberId);
		pointClient.processRefund(memberId, pointPaymentRequestDto);
	}
}
