package com.example.moduleticket;

import com.example.moduleticket.domain.ticket.dto.RefundDto;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefundQueueService {
	private final RedisTemplate<String, Object> refundObjectRedisTemplate;

	public void enqueueRefundTicket(List<RefundDto> refundDto) {
		refundObjectRedisTemplate.executePipelined((RedisCallback<Object>) connection -> {
			for (RefundDto dto : refundDto) {
				Map<byte[], byte[]> body = new HashMap<>();
				body.put("memberId".getBytes(), dto.getMemberId().toString().getBytes());
				body.put("price".getBytes(), String.valueOf(dto.getPrice()).getBytes());

				// XADD: stream 이름, 데이터 map
				connection.streamCommands().xAdd(
					"refund_stream".getBytes(),
					body
				);
			}
			return null;
		});
	}
}
