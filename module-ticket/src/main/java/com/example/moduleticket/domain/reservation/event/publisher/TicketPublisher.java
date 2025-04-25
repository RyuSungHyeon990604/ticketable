package com.example.moduleticket.domain.reservation.event.publisher;

import com.example.moduleticket.domain.reservation.event.TicketEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class TicketPublisher {

    private final RedisTemplate<String, Object> redisTemplate;

    public void publish(TicketEvent event) {
        redisTemplate.convertAndSend("reservation", event);
    }
}
