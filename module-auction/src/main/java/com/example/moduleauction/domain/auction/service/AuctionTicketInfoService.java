package com.example.moduleauction.domain.auction.service;

import java.util.stream.Collectors;

import com.example.moduleauction.domain.auction.dto.AuctionTicketInfoDto;
import com.example.moduleauction.domain.auction.entity.AuctionTicketInfo;
import com.example.moduleauction.domain.auction.repository.AuctionTicketInfoRepository;
import com.example.moduleauction.domain.ticket.entity.Ticket;
import com.example.moduleauction.feign.dto.TicketDto;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuctionTicketInfoService {

	private final AuctionTicketInfoRepository auctionTicketInfoRepository;

	@Transactional
	public AuctionTicketInfo createAuctionTicketInfo(TicketDto ticket) {
		String type = ticket.getSeatIds().get(0).getSection().getType();
		String code = ticket.getSeatIds().get(0).getSection().getCode();
		String sectionInfo = type + " | " + code;

		String seatInfo = ticket.getSeatIds().stream()
			.map(SeatIds::getPosition)
			.collect(Collectors.joining(" "));

		Integer seatCount = seats.size();

		Boolean isTogether = false;
		if (seats.size() > 1) {
			String prevRow = null;
			Integer prevColumn = null;
			isTogether = true; // 일단 연석이라고 가정하고, 조건을 깨면 false로 전환

			for (int i = 0; i < seats.size(); i++) {
				String[] parts = seats.get(i).getPosition().split("열 ");
				String currentRow = parts[0];
				int currentColumn = Integer.parseInt(parts[1].replaceAll("\\D", ""));

				if (i == 0) {
					prevRow = currentRow;
					prevColumn = currentColumn;
					continue;
				}

				// 행이 다르거나 열이 연속되지 않으면 연석 아님
				if (!prevRow.equals(currentRow) || currentColumn != prevColumn + 1) {
					isTogether = false;
					break;
				}

				prevColumn = currentColumn;
			}
		}

		AuctionTicketInfo auctionTicketInfo = AuctionTicketInfo.builder()
			.standardPoint(ticketInfo.getStandardPoint())
			.sectionInfo(ticketInfo.getSectionInfo())
			.seatInfo(ticketInfo.getSeatInfo())
			.seatCount(ticketInfo.getSeatCount())
			.isTogether(ticketInfo.getIsTogether())
			.build();

		return auctionTicketInfoRepository.save(auctionTicketInfo);
	}
}
