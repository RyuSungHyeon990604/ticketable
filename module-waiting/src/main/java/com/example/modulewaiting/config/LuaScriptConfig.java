package com.example.modulewaiting.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.script.DefaultRedisScript;

@Configuration
public class LuaScriptConfig {
	/**
	 * Redis Lua 스크립트를 통해 대기열(Waiting Queue)의 사용자들을 입장 큐(Proceed Queue)로 이동시키는 스크립트 Bean.
	 *
	 * <p>스크립트 동작 개요:
	 * <ul>
	 *     <li>현재 Proceed Queue의 사용자 수를 조회 (ZCARD)</li>
	 *     <li>남은 수용량(CAPACITY - 현재 인원)만큼 Waiting Queue에서 사용자 pop</li>
	 *     <li>pop된 사용자들을 Proceed Queue에 ZADD로 추가하고, Waiting Queue에서 ZREM</li>
	 *     <li>모든 작업은 원자적으로 처리됨</li>
	 * </ul>
	 *
	 * @return DefaultRedisScript - Lua 스크립트 실행을 위한 RedisScript Bean
	 */
	@Bean
	public DefaultRedisScript<Long> moveWaitingToProceedScript(){
		DefaultRedisScript<Long> script = new DefaultRedisScript<>();
		script.setResultType(Long.class);
		script.setLocation(new ClassPathResource("lua/moveWaitingToProceed.lua"));
		return script;
	}

	/**
	 * Redis Lua 스크립트를 통해 대기열(Waiting Queue)의 사용자들을 입장열로 이동시키는 스크립트 Bean.
	 *
	 * <p>스크립트 동작 개요:
	 * <ul>
	 *     <li>일정 크기 만큼(PROCEED_QUEUE_TARGET_SIZE) Waiting Queue에서 사용자 pop</li>
	 *     <li>pop된 사용자들을 입장열에 String Value로 추가</li>
	 *     <li>모든 작업은 원자적으로 처리됨</li>
	 * </ul>
	 *
	 * @return DefaultRedisScript - Lua 스크립트 실행을 위한 RedisScript Bean
	 */
	@Bean
	public DefaultRedisScript<Long> moveWaitingToProceedScriptV2(){
		DefaultRedisScript<Long> script = new DefaultRedisScript<>();
		script.setResultType(Long.class);
		script.setLocation(new ClassPathResource("lua/moveWaitingToProceedV2.lua"));
		return script;
	}
}
