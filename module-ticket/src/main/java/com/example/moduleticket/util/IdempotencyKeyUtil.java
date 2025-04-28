package com.example.moduleticket.util;

public class IdempotencyKeyUtil {

	// 예약 결제용 멱등키 생성: 예약 ID 기준
	public static String forReservation(Long reservationId, String action) {
		return "reservation:" + reservationId + ":" + action;
	}
}
