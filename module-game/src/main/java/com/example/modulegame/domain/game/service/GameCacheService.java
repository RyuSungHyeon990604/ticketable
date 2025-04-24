package com.example.modulegame.domain.game.service;


import com.example.modulegame.domain.game.entity.Game;
import com.example.modulegame.domain.game.repository.GameRepository;
import com.example.modulegame.domain.game.util.GameCacheHelper;
import com.example.modulegame.domain.stadium.dto.response.*;
import com.example.modulegame.domain.stadium.entity.Seat;
import com.example.modulegame.domain.stadium.entity.Stadium;
import com.example.modulegame.domain.stadium.service.SeatService;
import com.example.modulegame.feign.client.AuctionClient;
import com.example.modulegame.feign.client.TicketClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class GameCacheService {
    private final GameRepository gameRepository;
    private final CacheManager cacheManager;
    private final GameCacheHelper gameCacheHelper;
    private final TicketClient ticketClient;

    private final SeatService seatService;


    @Cacheable(value = "seatCountsBySectionType", key = "#gameId")
    public List<SectionTypeSeatCountResponse> getSeatCountsByTypeCached(Long gameId) {
        log.info("💡 캐시 미적중! DB에서 seat count 조회 - gameId: {}", gameId);
        // 좌석 ID 별 타입 정보 조회
        List<SectionTypeMapping> seats = gameRepository.findSeatsId(gameId);

        // 예약 된 좌석 ID 조회
        Set<Long> bookedSeatsId = ticketClient.getBookedSeatsId(gameId);

        // 게산 로직
        Map<String, Long> remainingCount = new LinkedHashMap<>();

        for (SectionTypeMapping seat : seats) {
            String type = seat.getSectionType();
            remainingCount.putIfAbsent(type,0L);
            if (!bookedSeatsId.contains(seat.getSeatId())) {
                remainingCount.put(type, remainingCount.get(type)+ 1);
            }
        }

        return remainingCount
                .entrySet()
                .stream()
                .map(entry -> new SectionTypeSeatCountResponse(entry.getKey(), entry.getValue()))
                .toList();
    }

    @Cacheable(value = "seatCountsBySectionCode", key = "T(String).format('%s:%s', #gameId, #type)")
    public List<SectionSeatCountResponse> getSeatCountsBySectionCached(Long gameId, String type) {
        log.info("💡 캐시 미적중! DB에서 seat count 조회 - gameId: {} - type {}", gameId, type);
        // 해당 타입의 구역별 좌석 ID 조회
        List<SectionCodeMapping> seats = gameRepository.findSeatsIdBySectionType(gameId, type);

        // 예매된 좌석 ID 조회
        Set<Long> bookedSeatsId = ticketClient.getBookedSeatsId(gameId);

        // 계산 로직
        Map<String, Long> remainingCount = new LinkedHashMap<>();

        for (SectionCodeMapping seat : seats) {
            String code = seat.getSectionCode();
            remainingCount.putIfAbsent(code,0L);
            if (!bookedSeatsId.contains(seat.getSeatId())) {
                remainingCount.put(code, remainingCount.get(code)+ 1);
            }
        }

        return remainingCount
                .entrySet()
                .stream()
                .map(entry -> new SectionSeatCountResponse(entry.getKey(), entry.getValue()))
                .toList();
    }

    @Cacheable(
            value = "gamesByCondition",  // 캐시 이름
            key = "T(String).format('%s:%s', #team == null ? 'all' : #team, #start != null ? #start.toLocalDate() : 'all')"  // 조건 조합 key
    )
    public List<Game> getGamesCached(String team, LocalDateTime start, LocalDateTime end) {
        log.info("💡 캐시 미적중! DB에서 game 조회");
        return gameRepository.findGames(team, start, end);
    }

    @Cacheable(
            value = "seat",  // 캐시 이름
            key = "T(String).format('%s:%s', #gameId, #sectionId)"  // 조건 조합 key
    )
    public List<SeatGetResponse> getSeatsCached(Long sectionId, Long gameId) {
        log.info("💡 캐시 미적중! DB에서 seat 조회");
        // 해당 구역의 좌석 ID 조회
        List<Seat> seats = seatService.getSeatsBySectionId(sectionId);
        // 예매된 좌석 ID 조회
        Set<Long> bookedSeatsId = ticketClient.getBookedSeatsId(gameId);

        // 계산 로직
        return seats.stream()
                .map(seat -> SeatGetResponse.of(seat, bookedSeatsId.contains(seat.getId())))
                .toList();
    }


//    public void handleAfterTicketChange(Long gameId) {
//        Cache cache = cacheManager.getCache("seatCountsBySectionType");
//
//        if (gameCacheHelper.isEvictStrategy(gameId+":")) {
//            cache.evict(gameId);
//            log.info("캐시 삭제");
//        } else {
//            List<SectionTypeMapping> updated =
//                    gameRepository.findSeatsId(gameId);
//            cache.put(gameId, updated);
//            log.info("캐시 갱신");
//        }
//    }
//
//    public void handleAfterTicketChangeByType(Long gameId, String type) {
//        Cache cache = cacheManager.getCache("seatCountsBySectionCode");
//        String key = String.format("%s:%s", gameId, type);
//        if (gameCacheHelper.isEvictStrategy(key)) {
//            cache.evict(key);
//            log.info("캐시 삭제");
//        } else {
//            List<SectionSeatCountResponse> updated =
//                    gameRepository.findSeatsIdBySectionType(gameId, type);
//            cache.put(key, updated);
//            log.info("캐시 갱신");
//        }
//    }
//
//    public void handleAfterTicketChangeBySeat(Long gameId, Long sectionId) {
//        Cache cache = cacheManager.getCache("seat");
//        String key = String.format("%s:%s", gameId, sectionId);
//        if (gameCacheHelper.isEvictStrategy(key)) {
//            cache.evict(key);
//            log.info("캐시 삭제");
//        } else {
//            List<SeatGetResponse> updated =
//                gameRepository.findSeatsInfo(sectionId, gameId).stream()
//                    .map(row -> new SeatGetResponse(
//                            ((Number) row[0]).longValue(),
//                            (String) row[1],
//                            (Boolean) row[2],
//                            ((Number) row[3]).intValue() == 1
//                    ))
//                    .toList();
//            cache.put(key,updated);
//            log.info("캐시 갱신");
//        }
//    }
//
//    public void handleAfterTicketChangeAll(Long gameId, Seat seat) {
//        String type = seat.getSection().getType();
//        handleAfterTicketChange(gameId);             // sectionType 캐시 처리
//        handleAfterTicketChangeByType(gameId, type); // sectionCode 캐시 처리
//        handleAfterTicketChangeBySeat(gameId, seat.getSection().getId());
//    }

    @Scheduled(cron = "0 0 0 * * *") // 매일 자정
    public void clearAllGameCaches() {
        Cache cache = cacheManager.getCache("gamesByCondition");
        if (cache != null) {
            cache.clear();
            log.info("🧹 gamesByCondition 캐시 전체 삭제 완료 (자정 정기 삭제)");
        }
    }
}
