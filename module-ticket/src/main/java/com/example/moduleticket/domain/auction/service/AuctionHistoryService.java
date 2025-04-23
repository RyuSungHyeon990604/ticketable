//package com.example.moduleticket.domain.auction.service;
//
//
//import static com.example.modulecommon.exception.ErrorCode.INVALID_BIDDING_AMOUNT;
//import static com.example.moduleticket.domain.auction.service.AuctionService.BID_UNIT;
//
//import com.example.modulecommon.exception.ServerException;
//import com.example.moduleticket.domain.auction.dto.request.AuctionBidRequest;
//import com.example.moduleticket.domain.auction.entity.Auction;
//import com.example.moduleticket.domain.auction.entity.AuctionHistory;
//import com.example.moduleticket.domain.auction.repository.AuctionHistoryRepository;
//import com.example.moduleticket.domain.member.entity.Member;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//@Service
//@RequiredArgsConstructor
//public class AuctionHistoryService {
//
//	private final AuctionHistoryRepository auctionHistoryRepository;
//
//	@Transactional
//	public void createAuctionHistory(Auction auction, Member bidder, AuctionBidRequest dto) {
//		// 6. 해당 경매기록에서, 가격이 같은 기록이 존재하면 예외처리
//		if (auctionHistoryRepository.existsByAuctionAndPoint(auction, dto.getCurrentBidPoint() + BID_UNIT)) {
//			throw new ServerException(INVALID_BIDDING_AMOUNT);
//		}
//
//		// 7. 경매기록 저장
//		AuctionHistory auctionHistory = AuctionHistory.builder()
//			.auction(auction)
//			.bidder(bidder)
//			.point(auction.getBidPoint() + BID_UNIT)
//			.build();
//		auctionHistoryRepository.save(auctionHistory);
//	}
//}
