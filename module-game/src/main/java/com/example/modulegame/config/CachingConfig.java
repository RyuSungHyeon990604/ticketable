package com.example.modulegame.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableCaching
@Configuration
public class CachingConfig {

    @Bean
    public CacheManager cacheManager() {
        SimpleCacheManager cacheManager = new SimpleCacheManager();

        Cache sectionCache = new CaffeineCache("seatCountsBySectionType",
                Caffeine.newBuilder()
                        .expireAfterWrite(12, TimeUnit.HOURS)
                        .build());
        Cache sectionTypeCache = new CaffeineCache("seatCountsBySectionCode",
                Caffeine.newBuilder()
                        .expireAfterWrite(12, TimeUnit.HOURS)
                        .build());
        Cache seatCache = new CaffeineCache("seat",
                Caffeine.newBuilder()
                        .expireAfterWrite(12, TimeUnit.HOURS)
                        .build());
        Cache gameCache = new CaffeineCache("gamesByCondition",
                Caffeine.newBuilder()
                        .build());

        cacheManager.setCaches(List.of(sectionCache, sectionTypeCache, seatCache, gameCache));
        return cacheManager;
    }
// 추후 분산 서버로 인한 레디스캐싱으로 변경 시 사용 예정
//@Bean
//public RedisCacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
//    // Jackson2JsonRedisSerializer로 JSON 직렬화 설정
//    ObjectMapper objectMapper = new ObjectMapper();
//    objectMapper.registerModule(new JavaTimeModule());
//    objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
//
//    RedisSerializer<Object> serializer = new GenericJackson2JsonRedisSerializer(objectMapper);
//
//    RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
//            .entryTtl(Duration.ofMinutes(30))
//            .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(serializer));
//
//
//    return RedisCacheManager.builder(redisConnectionFactory)
//            .cacheDefaults(config)
//            .build();
//    }
}
