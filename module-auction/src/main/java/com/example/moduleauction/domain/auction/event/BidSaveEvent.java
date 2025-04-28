package com.example.moduleauction.domain.auction.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class BidSaveEvent {

	private final Long auctionId;
	private final Integer bidPoint;
}