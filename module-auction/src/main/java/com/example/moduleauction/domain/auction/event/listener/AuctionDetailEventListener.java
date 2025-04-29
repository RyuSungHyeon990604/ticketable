package com.example.moduleauction.domain.auction.event.listener;

import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.example.moduleauction.domain.auction.event.AuctionDetailSaveEvent;
import com.example.moduleauction.util.AuctionDetailRedisUtil;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AuctionDetailEventListener {

	private final AuctionDetailRedisUtil auctionDetailRedisUtil;

	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	public void handleTicketInfoSaveEvent(AuctionDetailSaveEvent event) {
		auctionDetailRedisUtil.saveAuctionDetail(event.getAuctionId(), event.getAuctionDetailDto());
	}
}
