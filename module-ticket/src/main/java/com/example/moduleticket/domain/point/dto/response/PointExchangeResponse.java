package com.example.moduleticket.domain.point.dto.response;

import com.example.moduleticket.domain.point.enums.PointHistoryType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class PointExchangeResponse {

	private final Long memberId;
	private final Integer charge;
	private final Integer point;
	private final PointHistoryType type;
}
