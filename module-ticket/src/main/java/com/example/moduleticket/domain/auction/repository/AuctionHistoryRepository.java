//package com.example.moduleticket.domain.auction.repository;
//
//
//import com.example.moduleticket.domain.auction.entity.Auction;
//import com.example.moduleticket.domain.auction.entity.AuctionHistory;
//import com.example.moduleticket.domain.ticket.entity.Ticket;
//import org.springframework.data.jpa.repository.JpaRepository;
//
//public interface AuctionHistoryRepository extends JpaRepository<AuctionHistory, Long> {
//	boolean existsByAuctionAndPoint(Auction auction, Integer point);
//	boolean existsByAuction_Ticket(Ticket ticket);
//}
