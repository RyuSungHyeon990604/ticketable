package com.example.modulepoint.domain.point.service;

import static com.example.modulecommon.exception.ErrorCode.CAN_NOT_EXCHANGE;
import static com.example.modulecommon.exception.ErrorCode.EXCHANGE_WAITING;
import static com.example.modulecommon.exception.ErrorCode.NOT_ENOUGH_POINT;
import static com.example.modulecommon.exception.ErrorCode.USER_NOT_FOUND;
import static com.example.modulepoint.domain.point.enums.PointHistoryType.EXCHANGE_REQUEST;

import com.example.modulecommon.exception.ServerException;
import com.example.modulepoint.domain.point.dto.request.ExchangePointRequest;
import com.example.modulepoint.domain.point.dto.response.PointResponse;
import com.example.modulepoint.domain.point.entity.Point;
import com.example.modulepoint.domain.point.enums.PointHistoryType;
import com.example.modulepoint.domain.point.repository.PointHistoryRepository;
import com.example.modulepoint.domain.point.repository.PointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class PointService {

	private final PointRepository pointRepository;
	private final PointHistoryService pointHistoryService;
	private final PointHistoryRepository pointHistoryRepository;

	/**
	 * TODO : 포인트를 감소시키고, 환전 유저의 계좌에 돈을 보내는 로직 추가해야함
	 */
	@Transactional
	public PointResponse exchangePoint(Long memberId, ExchangePointRequest request) {
		if(request.getPoint() < 100) {
			throw new ServerException(CAN_NOT_EXCHANGE);
		}

		Point point = getPoint(memberId);
		if (point.getPoint() < request.getPoint()) {
			throw new ServerException(NOT_ENOUGH_POINT);
		}

		if (pointHistoryRepository.existsByMemberIdAndType(memberId, EXCHANGE_REQUEST)) {
			throw new ServerException(EXCHANGE_WAITING);
		}

		point.minusPoint(request.getPoint());
		pointHistoryService.createPointHistory(request.getPoint(), EXCHANGE_REQUEST, memberId);
		return new PointResponse(memberId, request.getPoint());
	}

	@Transactional(readOnly = true)
	public PointResponse getMemberPoint(Long memberId) {
		Point point = getPoint(memberId);
		return PointResponse.of(point);
	}

	@Transactional
	public void createPoint(Long memberId) {
		Point point = Point.builder()
			.point(0)
			.memberId(memberId)
			.build();

		pointRepository.save(point);
	}

	/**
	 * 입찰 실패, 포인트 충전, 환불, 판매 등에서 사용될 포인트 증가 메서드
	 */
	@Transactional
	public void increasePoint(Long memberId, Integer charge, PointHistoryType type) {
		Point point = getPoint(memberId);

		point.plusPoint(charge);
		pointHistoryService.createPointHistory(charge, type, memberId);
	}

	/**
	 * 입찰, 티켓 예매 등에서 사용될 포인트 증가 메서드
	 */
	@Transactional
	public void decreasePoint(Long memberId, Integer charge, PointHistoryType type) {
		Point point = getPoint(memberId);

		point.minusPoint(charge);
		pointHistoryService.createPointHistory(charge, type, memberId);
	}

	/**
	 * 해당 멤버 아이디를 통해 해당 멤버의 포인트를 가져옴.
	 * 만약 멤버가 존재하지 않거나, 삭제되었다면 예외를 던짐
	 */
	private Point getPoint(Long memberId) {
		return pointRepository.findByMemberId(memberId)
			.orElseThrow(() -> new ServerException(USER_NOT_FOUND));
	}
}
