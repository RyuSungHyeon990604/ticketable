package com.example.modulepoint.domain.point.service;

import com.example.modulepoint.domain.point.dto.RefundDto;
import com.example.modulepoint.domain.point.enums.PointHistoryType;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.connection.stream.Consumer;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.connection.stream.ReadOffset;
import org.springframework.data.redis.connection.stream.StreamOffset;
import org.springframework.data.redis.connection.stream.StreamReadOptions;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class RefundStreamConsumer {
	private final RedisTemplate<String, Object> redisTemplate;
	private final PointService pointService;

	private static final String STREAM_KEY = "refund_stream";
	private static final String GROUP_NAME = "refund_group";
	private static final String CONSUMER_NAME = "refund-consumer-1";

	@Async
	@EventListener(ApplicationReadyEvent.class)
	public void runWorker() {
		processRefunds();
	}

	public void processRefunds() {
		while (true) {
			try {
				List<MapRecord<String, Object, Object>> messages =
					redisTemplate.opsForStream().read(
						Consumer.from(GROUP_NAME, CONSUMER_NAME),
						StreamReadOptions.empty().count(10).block(Duration.ofSeconds(30)),
						StreamOffset.create(STREAM_KEY, ReadOffset.lastConsumed())
					);

				if (messages != null) {
					//todo : 벌크 업데이트 처리
					for (MapRecord<String, Object, Object> message : messages) {
						RefundDto refund = mapToRefundDto(message.getValue());
						try {
							pointService.increasePoint(refund.getMemberId(), refund.getPrice(), PointHistoryType.REFUND);
							redisTemplate.opsForStream().acknowledge(STREAM_KEY, GROUP_NAME, message.getId());
							log.info("처리 완료: {}", refund.getMemberId());
						} catch (Exception e) {
							log.error("처리 실패, 재시도 필요: {}", refund.getMemberId(), e);
							// 필요 시 DLQ(Dead Letter Queue)로 처리
						}
					}
				}
			} catch (Exception e) {
				String rootMessage = e.getCause() != null ? e.getCause().getMessage() : e.getMessage();

				//운영중 스트림이 삭제됐을때
				if(rootMessage.contains("NOGROUP")) {
					log.warn("그룹이 존재하지 않습니다. 재생성 시도...");
					try {
						redisTemplate.opsForStream().createGroup(STREAM_KEY, ReadOffset.from("0"), GROUP_NAME);
						log.info("그룹 재생성 완료");
					} catch (Exception innerEx) {
						log.error("그룹 재생성 실패: {}", innerEx.getMessage());
						throw e;
					}
				} else {
					throw e;
				}
			}
		}
	}
	private RefundDto mapToRefundDto(Map<Object, Object> valueMap) {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.convertValue(valueMap, RefundDto.class);
	}
}
