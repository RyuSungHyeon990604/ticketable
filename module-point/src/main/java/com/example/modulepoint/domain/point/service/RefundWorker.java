package com.example.modulepoint.domain.point.service;

import com.example.modulepoint.domain.point.dto.RefundDto;
import com.example.modulepoint.domain.point.enums.PointHistoryType;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class RefundWorker implements ApplicationListener<ApplicationReadyEvent> {

	private final RefundQueueService refundQueueService;
	private final PointService pointService;

	@Override
	public void onApplicationEvent(ApplicationReadyEvent event) {
		runWorker();
	}

	@Async
	public void runWorker() {
		processRefunds();
	}


	public void processRefunds() {
		while (true) {
			RefundDto refundDto = refundQueueService.blockingPopRefundTicket();
			if (refundDto != null) {
				try {
					pointService.increasePoint(refundDto.getMemberId(), refundDto.getPrice(), PointHistoryType.REFUND);
					log.info("✅ 환불 성공: MemberId={}", refundDto.getMemberId());
				} catch (Exception e) {
					log.error("❌ 환불 실패, 재시도 예정: MemberId={}", refundDto.getMemberId() , e);
					refundQueueService.enqueueRefundTicket(refundDto);
				}
			}
		}
	}
}

