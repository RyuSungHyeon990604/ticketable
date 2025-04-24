package com.example.moduleauction.domain.auction.event.listener;

import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.example.moduleauction.domain.auction.event.BidUpdateEvent;
import com.example.moduleauction.util.AuctionBidRedisUtil;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class BidUpdateEventListener {

	private final AuctionBidRedisUtil auctionBidRedisUtil;

	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	public void handleBidUpdateEvent(BidUpdateEvent event) {
		auctionBidRedisUtil.updateBidKey(event.getAuctionId(), event.getNextBid());
	}
}
