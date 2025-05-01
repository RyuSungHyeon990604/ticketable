package com.example.modulepoint.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.RedisSystemException;
import org.springframework.data.redis.connection.stream.ReadOffset;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class RedisStreamInitializer {
	private final RedisTemplate<String, Object> redisTemplate;
	private static final String STREAM_KEY = "refund_stream";
	private static final String GROUP_NAME = "refund_group";


	@EventListener(ApplicationReadyEvent.class)
	public void run() {
		try {
			redisTemplate.opsForStream().createGroup(STREAM_KEY, ReadOffset.from("0"), GROUP_NAME);
			log.info("Created Redis Stream Group: " + GROUP_NAME);
		} catch (RedisSystemException e) {
			String rootMessage = e.getCause() != null ? e.getCause().getMessage() : e.getMessage();
			log.info(rootMessage);
			//이미 생성된 컨슈머 그룹이 존재할경우
			if(rootMessage.contains("BUSYGROUP")) {
				log.info("Group already exists: {}", GROUP_NAME);
			} else {
				log.warn("ailed to create stream group '{}': {}", GROUP_NAME, rootMessage);
				throw e;
			}
		}
	}
}
