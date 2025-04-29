package com.example.modulepoint.domain.point.service;

import com.example.modulepoint.domain.point.dto.RefundDto;
import com.example.modulepoint.domain.point.enums.PointHistoryType;
import io.lettuce.core.RedisCommandTimeoutException;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class RefundWorker {

	private final RefundQueueService refundQueueService;
	private final PointService pointService;

	@Async
	@EventListener(ApplicationReadyEvent.class)
	public void runWorker() {
		processRefunds();
	}

	public void processRefunds() {
		while (true) {
			try {
				RefundDto refundDto = refundQueueService.blockingPopRefundTicket();
				if (refundDto != null) {
					try {
						pointService.increasePoint(refundDto.getMemberId(), refundDto.getPrice(), PointHistoryType.REFUND);
						log.info("환불 성공: MemberId={}", refundDto.getMemberId());
					} catch (Exception e) {
						log.error("환불 실패, 재시도 예정: MemberId={}", refundDto.getMemberId() , e);
						refundQueueService.enqueueRefundTicket(refundDto);
					}
				}
			} catch (RedisConnectionFailureException | RedisCommandTimeoutException e) {
				log.warn("Redis 연결 실패 혹은 Timeout 발생. 재시도 대기중...");
				try {
					Thread.sleep(1000); // 3초 쉬고 재시도
				} catch (InterruptedException ignored) {
					Thread.currentThread().interrupt();
				}
			}

		}
	}
}

