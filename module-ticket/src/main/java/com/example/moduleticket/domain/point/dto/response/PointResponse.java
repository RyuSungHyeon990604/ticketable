//package com.example.moduleticket.domain.point.dto.response;
//
//import com.example.moduleticket.domain.point.entity.Point;
//import lombok.Getter;
//import lombok.RequiredArgsConstructor;
//
//@Getter
//@RequiredArgsConstructor
//public class PointResponse {
//
//	private final Long memberId;
//	private final Integer point;
//
//	public static PointResponse of(Point point) {
//		return new PointResponse(
//			point.getMember().getId(),
//			point.getPoint()
//		);
//	}
//}
