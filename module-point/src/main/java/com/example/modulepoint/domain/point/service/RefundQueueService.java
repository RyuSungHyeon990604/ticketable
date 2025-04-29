package com.example.modulepoint.domain.point.service;

import com.example.modulepoint.domain.point.dto.RefundDto;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefundQueueService {
	private static final String REFUND_QUEUE_KEY = "refund-queue";
	private final RedisTemplate<String, RefundDto> refundRedisTemplate;

	public void enqueueRefundTicket(RefundDto refundDto) {
		refundRedisTemplate.opsForList().leftPush(REFUND_QUEUE_KEY, refundDto);
	}

	public RefundDto blockingPopRefundTicket() {
		RefundDto result = refundRedisTemplate.opsForList()
			.rightPop(REFUND_QUEUE_KEY, Duration.ofSeconds(0)); // 0초 => 무한 대기
		return result != null  ? result : null;
	}
}
