package com.example.moduleticket.domain.queue.service;

import static com.example.modulecommon.exception.ErrorCode.INVALID_WAITING_TOKEN;

import com.example.modulecommon.exception.ServerException;
import com.example.moduleticket.domain.queue.QueueSystemConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class QueueManager {
	private final WaitingQueueService waitingQueueService;
	private final ProceedQueueService proceedQueueService;

	public QueueManager(WaitingQueueService waitingQueueService, @Qualifier("redisProceedQueueServiceV2") ProceedQueueService proceedQueueService) {
		this.waitingQueueService = waitingQueueService;
		this.proceedQueueService = proceedQueueService;
	}
	//대기열 입장
	public String enterWaitingQueue() {
		return waitingQueueService.enterWaitingQueue();
	}

	//대기순번 조회
	public long getWaitingOrder(String token) {
		long waitingOrder = waitingQueueService.getOrder(token);
		boolean isProceed = proceedQueueService.isContains(token);
		//대기열, 작업열 에 존재하지않으면 잘못된 토큰
		if(waitingOrder == -1 && !isProceed) {
			log.warn("{} : {}", INVALID_WAITING_TOKEN.getMessage(), token);
			throw new ServerException(INVALID_WAITING_TOKEN);
		}
		return waitingOrder;
	}

	//해당 토큰이 작업이 가능한 상태인지 조회
	public boolean isAllowed(String token) {
		return proceedQueueService.isContains(token);
	}

	public void removeTokenFromProceedQueue(String token) {
		proceedQueueService.removeToken(token);
	}

	public void deleteTokenFromWaitingAndProceedQueue(String token) {
		waitingQueueService.removeToken(token);
		proceedQueueService.removeToken(token);
	}

	public long getExpectedWaitingOrder(long currentWaitingOrder) {
		double v = QueueSystemConstants.PROCEED_QUEUE_TARGET_SIZE * 0.8;
		return (long) Math.floor(currentWaitingOrder / v) + 1;
	}

	@Scheduled(fixedRate = 1000)
	public void moveWaitingToProceedAtomicScheduled(){
		proceedQueueService.pullFromWaitingQueue(QueueSystemConstants.PROCEED_QUEUE_TARGET_SIZE);
	}

	@Scheduled(fixedRate = 10000)
	public void removeExpiredTokenFromProceedQueue(){
		proceedQueueService.removeExpiredTokens();
	}
}
