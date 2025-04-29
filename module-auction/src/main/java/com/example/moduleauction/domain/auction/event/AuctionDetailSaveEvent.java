package com.example.moduleauction.domain.auction.event;

import com.example.moduleauction.domain.auction.dto.AuctionDetailDto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class AuctionDetailSaveEvent {

	private final Long auctionId;
	private final AuctionDetailDto auctionDetailDto;
}
