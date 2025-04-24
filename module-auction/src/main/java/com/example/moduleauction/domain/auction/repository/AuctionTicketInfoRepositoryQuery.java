package com.example.moduleauction.domain.auction.repository;


import com.example.moduleauction.domain.auction.dto.AuctionTicketInfoDto;
import com.example.moduleauction.domain.ticket.entity.Ticket;

public interface AuctionTicketInfoRepositoryQuery {
	AuctionTicketInfoDto findTicketInfo(Ticket ticket);
}
