//package com.example.moduleticket.domain.point.service;
//
//
//import static com.example.modulecommon.exception.ErrorCode.EXCHANGE_REQUEST_NOT_STATE;
//import static com.example.modulecommon.exception.ErrorCode.POINT_HISTORY_NOT_FOUND;
//import static com.example.modulecommon.exception.ErrorCode.USER_NOT_FOUND;
//import static com.example.moduleticket.domain.point.enums.PointHistoryType.EXCHANGE_REQUEST;
//
//import com.example.modulecommon.exception.ServerException;
//import com.example.moduleticket.domain.point.dto.response.PointExchangeResponse;
//import com.example.moduleticket.domain.point.dto.response.PointHistoryResponse;
//import com.example.moduleticket.domain.point.entity.Point;
//import com.example.moduleticket.domain.point.entity.PointHistory;
//import com.example.moduleticket.domain.point.repository.PointHistoryRepository;
//import com.example.moduleticket.domain.point.repository.PointRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.domain.Sort;
//import org.springframework.data.web.PagedModel;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//@Service
//@RequiredArgsConstructor
//public class PointExchangeService {
//
//	private final PointHistoryRepository pointHistoryRepository;
//	private final PointRepository pointRepository;
//
//	@Transactional
//	public PointExchangeResponse exchangePoint(Auth auth, Long pointHistoryId) {
//		PointHistory pointHistory = pointHistoryRepository.findById(pointHistoryId)
//			.orElseThrow(() -> new ServerException(POINT_HISTORY_NOT_FOUND));
//
//		if (!pointHistory.getType().equals(EXCHANGE_REQUEST)) {
//			throw new ServerException(EXCHANGE_REQUEST_NOT_STATE);
//		}
//
//		Long memberId = pointHistory.getMember().getId();
//		Point point = pointRepository.findByMemberId(memberId)
//			.orElseThrow(() -> new ServerException(USER_NOT_FOUND));
//
//		pointHistory.exchange();
//
//		return new PointExchangeResponse(memberId, pointHistory.getCharge(), point.getPoint(), pointHistory.getType());
//	}
//
//	@Transactional(readOnly = true)
//	public PointHistoryResponse getExchangeRequestPointHistory(Auth auth, Long pointHistoryId) {
//		PointHistory pointHistory = getPointHistory(pointHistoryId);
//		return PointHistoryResponse.of(pointHistory);
//	}
//
//	@Transactional(readOnly = true)
//	public PagedModel<PointHistoryResponse> getExchangeRequestPointHistories(Auth auth, int page) {
//		Pageable pageable = PageRequest.of(page - 1, 10,
//			Sort.by(Sort.Direction.ASC, "createdAt"));
//
//		Page<PointHistory> points = pointHistoryRepository.findAllByType(EXCHANGE_REQUEST, pageable);
//
//		return new PagedModel<>(points.map(PointHistoryResponse::of));
//	}
//
//	private PointHistory getPointHistory(Long pointHistoryId) {
//		return pointHistoryRepository.findById(pointHistoryId)
//			.orElseThrow(() -> new ServerException(POINT_HISTORY_NOT_FOUND));
//	}
//}
