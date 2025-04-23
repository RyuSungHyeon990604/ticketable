//package com.example.moduleticket.domain.auction.service;
//
//import com.example.ticketable.domain.auction.dto.AuctionTicketInfoDto;
//import com.example.ticketable.domain.auction.entity.AuctionTicketInfo;
//import com.example.ticketable.domain.auction.repository.AuctionTicketInfoRepository;
//import com.example.ticketable.domain.ticket.entity.Ticket;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//@Service
//@RequiredArgsConstructor
//public class AuctionTicketInfoService {
//
//	private final AuctionTicketInfoRepository auctionTicketInfoRepository;
//
//	@Transactional
//	public AuctionTicketInfo createAuctionTicketInfo(Ticket ticket) {
//		AuctionTicketInfoDto ticketInfo = auctionTicketInfoRepository.findTicketInfo(ticket);
//
//		AuctionTicketInfo auctionTicketInfo = AuctionTicketInfo.builder()
//			.standardPoint(ticketInfo.getStandardPoint())
//			.sectionInfo(ticketInfo.getSectionInfo())
//			.seatInfo(ticketInfo.getSeatInfo())
//			.seatCount(ticketInfo.getSeatCount())
//			.isTogether(ticketInfo.getIsTogether())
//			.build();
//
//		return auctionTicketInfoRepository.save(auctionTicketInfo);
//	}
//}
