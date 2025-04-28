package com.example.moduleauction.domain.auction.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.example.moduleauction.domain.auction.entity.Auction;
import com.example.moduleauction.domain.auction.entity.AuctionTicketInfo;
import com.example.moduleauction.feign.dto.GameDto;
import com.example.moduleauction.feign.dto.SectionAndPositionDto;
import com.example.moduleauction.feign.dto.TicketDto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
@AllArgsConstructor
public class AuctionDetailDto {

	private final Long id;

	private final Integer startPoint;

	private final Integer standardPoint;

	private final String sectionType;

	private final String sectionCode;

	private final String seatInfo;

	private final Integer seatCount;

	private final Boolean isTogether;

	private final LocalDateTime gameStartTime;

	private final String home;

	private final String away;

	private final String gameType;

	private final LocalDateTime createdAt;

	public static AuctionDetailDto of(Auction auction, AuctionTicketInfo auctionTicketInfo, TicketDto ticketDto,
		GameDto gameDto, SectionAndPositionDto sectionAndPositionDto, List<String> sortedPositions) {
		return new AuctionDetailDto(
			auction.getId(),
			auction.getStartPoint(),
			ticketDto.getTotalPoint(),
			sectionAndPositionDto.getType(),
			sectionAndPositionDto.getCode(),
			String.join(", ", sortedPositions),
			auctionTicketInfo.getSeatCount(),
			auctionTicketInfo.getIsTogether(),
			auctionTicketInfo.getGameStartTime(),
			auctionTicketInfo.getHome(),
			auctionTicketInfo.getAway(),
			gameDto.getType(),
			auction.getCreatedAt()
		);
	}
}
