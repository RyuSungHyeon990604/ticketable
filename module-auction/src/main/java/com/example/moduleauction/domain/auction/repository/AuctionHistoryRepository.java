package com.example.moduleauction.domain.auction.repository;

import com.example.moduleauction.domain.auction.entity.Auction;
import com.example.moduleauction.domain.auction.entity.AuctionHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AuctionHistoryRepository extends JpaRepository<AuctionHistory, Long> {
	boolean existsByAuctionAndPoint(Auction auction, Integer point);
	@Query("SELECT COUNT(ah) > 0 "
		+ "   FROM AuctionHistory ah "
		+ "  WHERE ah.auction.ticketId = :ticketId")
	boolean existsByTicketId(Long ticketId);
}
