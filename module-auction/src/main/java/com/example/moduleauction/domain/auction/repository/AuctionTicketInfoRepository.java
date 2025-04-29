package com.example.moduleauction.domain.auction.repository;


import com.example.moduleauction.domain.auction.entity.AuctionTicketInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuctionTicketInfoRepository extends JpaRepository<AuctionTicketInfo, Long>{
}
