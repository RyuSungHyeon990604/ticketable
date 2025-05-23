package com.example.modulepoint.domain.point.dto.response;

import com.example.modulepoint.domain.point.entity.PointHistory;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class PointHistoryResponse {

	private final Long memberId;
	private final String type;
	private final Integer charge;
	private final LocalDateTime createdAt;

	public static PointHistoryResponse of(PointHistory pointHistory) {
		return new PointHistoryResponse(
			pointHistory.getMemberId(),
			pointHistory.getType().toString(),
			pointHistory.getCharge(),
			pointHistory.getCreatedAt()
		);
	}
}
