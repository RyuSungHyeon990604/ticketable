package com.example.modulepoint.domain.exchange.dto.response;

import com.example.modulepoint.domain.exchange.enums.ExchangeHistoryType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class ExchangeResponse {

	private final Long memberId;
	private final Integer charge;
	private final Integer point;
	private final String type;
}
