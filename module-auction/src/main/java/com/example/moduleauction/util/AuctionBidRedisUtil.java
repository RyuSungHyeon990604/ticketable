package com.example.moduleauction.util;

import static com.example.modulecommon.exception.ErrorCode.INVALID_BIDDING_AMOUNT;

import com.example.modulecommon.exception.ServerException;
import com.example.moduleauction.domain.auction.entity.Auction;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuctionBidRedisUtil {

	private final RedisTemplate<String, String> redisTemplate;

	private static final Duration AUCTION_BID_TTL = Duration.ofHours(24);

	public void createBidKey(Auction auction) {
		String key = buildKey(auction.getId());
		redisTemplate.opsForValue().set(key, String.valueOf(auction.getBidPoint()), AUCTION_BID_TTL);
	}

	public Integer getBidPoint(Long auctionId) {
		String value = redisTemplate.opsForValue().get(buildKey(auctionId));
		if (value == null) return null;
		try {
			return Integer.parseInt(value);
		} catch (NumberFormatException e) {
			return null;
		}
	}

	public void validateBid(Long auctionId, int currentBid) {
		String key = buildKey(auctionId);
		String cachedBid = redisTemplate.opsForValue().get(key);
		if (cachedBid == null || !cachedBid.equals(String.valueOf(currentBid))) {
			throw new ServerException(INVALID_BIDDING_AMOUNT);
		}
	}

	public void updateBidKey(Long auctionId, Integer nextBid) {
		String key = buildKey(auctionId);
		redisTemplate.opsForValue().set(key, String.valueOf(nextBid));
	}

	public void deleteBidKey(Long auctionId) {
		String key = buildKey(auctionId);
		redisTemplate.delete(key);
	}

	private static String buildKey(Long auction) {
		return "auction:" + auction + ":bid";
	}
}