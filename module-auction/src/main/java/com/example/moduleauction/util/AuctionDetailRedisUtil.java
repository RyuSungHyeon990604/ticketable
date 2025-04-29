package com.example.moduleauction.util;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.example.moduleauction.domain.auction.dto.AuctionDetailDto;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AuctionDetailRedisUtil {

	private final RedisTemplate<String, Object> redisTemplate;
	private static final String PREFIX = "auction:detail:";
	private static final Duration AUCTION_DETAIL_TTL = Duration.ofHours(24);

	public void saveAuctionDetail(Long auctionId, AuctionDetailDto auctionDetailDto) {
		String key = buildKey(auctionId);
		redisTemplate.opsForValue().set(key, auctionDetailDto, AUCTION_DETAIL_TTL);
	}

	public AuctionDetailDto getAuctionDetail(Long auctionId) {
		String key = buildKey(auctionId);
		return (AuctionDetailDto) redisTemplate.opsForValue().get(key);
	}

	public Map<Long, AuctionDetailDto> getAuctionDetails(List<Long> auctionIds) {
		List<String> keys = auctionIds.stream()
			.map(AuctionDetailRedisUtil::buildKey)
			.toList();

		List<Object> values = redisTemplate.opsForValue().multiGet(keys);
		if (Objects.requireNonNull(values).isEmpty()) {
			return null;
		}

		Map<Long, AuctionDetailDto> result = new HashMap<>();

		for (int i = 0; i < auctionIds.size(); i++) {
			Object value = values.get(i);
			if (value instanceof AuctionDetailDto auctionDetailDto) {
				result.put(auctionIds.get(i), auctionDetailDto);
			}
		}
		return result;
	}

	public void deleteAuctionDetail(Long auctionId) {
		String key = buildKey(auctionId);
		redisTemplate.delete(key);
	}

	private static String buildKey(Long auctionId) {
		return PREFIX + auctionId;
	}
}

