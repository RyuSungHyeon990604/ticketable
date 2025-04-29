package com.example.modulepoint.domain.exchange.dto.response;

import com.example.modulepoint.domain.exchange.entity.ExchangeHistory;
import com.example.modulepoint.domain.exchange.enums.ExchangeHistoryType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ExchangeHistoryResponse {
	
	private final Long memberId;
	private final Integer charge;
	private final String type;
	private final LocalDateTime createdAt;
	
	public static ExchangeHistoryResponse of(ExchangeHistory exchangeHistory) {
		return new ExchangeHistoryResponse(
			exchangeHistory.getMemberId(),
			exchangeHistory.getCharge(),
			exchangeHistory.getType().toString(),
			exchangeHistory.getCreatedAt()
		);
	}
}
