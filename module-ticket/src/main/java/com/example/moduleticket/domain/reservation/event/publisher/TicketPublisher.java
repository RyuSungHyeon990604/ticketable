package com.example.moduleticket.domain.reservation.event.publisher;

import com.example.moduleticket.domain.reservation.event.TicketEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Slf4j
public class TicketPublisher {

    private final RedisTemplate<String, Object> redisTemplate;

    public void publish(TicketEvent event) {
        log.info("캐시 초기화를 위한 이벤트 발행");
        redisTemplate.convertAndSend("reservation", event);
    }
}
