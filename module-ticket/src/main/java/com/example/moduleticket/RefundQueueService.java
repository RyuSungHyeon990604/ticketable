package com.example.moduleticket;

import com.example.moduleticket.domain.ticket.dto.RefundDto;
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
}
