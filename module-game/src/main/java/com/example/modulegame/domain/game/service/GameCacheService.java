package com.example.modulegame.domain.game.service;


import com.example.modulegame.domain.game.dto.response.GameGetResponse;
import com.example.modulegame.domain.game.repository.GameRepository;
import com.example.modulegame.domain.game.util.GameCacheHelper;
import com.example.modulegame.domain.stadium.dto.response.*;
import com.example.modulegame.domain.stadium.entity.Seat;
import com.example.modulegame.domain.stadium.entity.Section;
import com.example.modulegame.domain.stadium.service.SeatService;
import com.example.modulegame.domain.stadium.service.SectionService;
import com.example.modulegame.feign.client.ReservationClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final ReservationClient reservationClient;
    private final SeatService seatService;


    @Cacheable(value = "seatCountsBySectionType", key = "#gameId")
    public List<SectionTypeSeatCountResponse> getSeatCountsByTypeCached(Long gameId) {
        log.info("💡 캐시 미적중! DB에서 seat count 조회 - gameId: {}", gameId);
        return calculateSeatCountBySectionType(gameId);
    }

    @Cacheable(value = "seatCountsBySectionCode", key = "T(String).format('%s:%s', #gameId, #type)")
    public List<SectionSeatCountResponse> getSeatCountsBySectionCached(Long gameId, String type) {
        log.info("💡 캐시 미적중! DB에서 seat count 조회 - gameId: {} - type {}", gameId, type);
        return calculateSeatCountBySection(gameId, type);
    }

    @Cacheable(
            value = "gamesByCondition",  // 캐시 이름
            key = "T(String).format('%s:%s', #team == null ? 'all' : #team, #start != null ? #start.toLocalDate() : 'all')"  // 조건 조합 key
    )
    public List<GameGetResponse> getGamesCached(String team, LocalDateTime start, LocalDateTime end) {
        log.info("💡 캐시 미적중! DB에서 game 조회");
        return gameRepository.findGames(team, start, end).stream().map(GameGetResponse::of).toList();
    }

    @Cacheable(
            value = "seat",  // 캐시 이름
            key = "T(String).format('%s:%s', #gameId, #sectionId)"  // 조건 조합 key
    )
    public List<SeatGetResponse> getSeatsCached(Long sectionId, Long gameId) {
        log.info("💡 캐시 미적중! DB에서 seat 조회");
        return seatInfo(gameId, sectionId);
    }


    public void handleAfterTicketChange(Long gameId, Set<Long> bookedSeatsId) {
        Cache cache = cacheManager.getCache("seatCountsBySectionType");

        if (gameCacheHelper.isEvictStrategy(gameId+":")) {
            cache.evict(gameId);
            log.info("캐시 삭제");
        } else {
            cache.put(gameId, calculateSeatCountBySectionType(gameId, bookedSeatsId));
            log.info("캐시 갱신");
        }
    }

    public void handleAfterTicketChangeByType(Long gameId, String type, Set<Long> bookedSeatsId) {
        Cache cache = cacheManager.getCache("seatCountsBySectionCode");
        String key = String.format("%s:%s", gameId, type);
        if (gameCacheHelper.isEvictStrategy(key)) {
            cache.evict(key);
            log.info("캐시 삭제");
        } else {
            cache.put(key, calculateSeatCountBySection(gameId, type, bookedSeatsId));
            log.info("캐시 갱신");
        }
    }

    public void handleAfterTicketChangeBySeat(Long gameId, Long sectionId, Set<Long> bookedSeatsId) {
        Cache cache = cacheManager.getCache("seat");
        String key = String.format("%s:%s", gameId, sectionId);
        if (gameCacheHelper.isEvictStrategy(key)) {
            cache.evict(key);
            log.info("캐시 삭제");
        } else {
            cache.put(key,seatInfo(gameId, sectionId, bookedSeatsId));
            log.info("캐시 갱신");
        }
    }

    @Transactional
    public void handleAfterTicketChangeAll(Long gameId, Long seatId) {
        Seat seat = seatService.getSeat(seatId);
        String sectionType = seat.getSection().getType();
        Long sectionId = seat.getSection().getId();
        Set<Long> bookedSeatsId = reservationClient.getBookedSeatsId(gameId);
        handleAfterTicketChange(gameId, bookedSeatsId);             // sectionType 캐시 처리
        handleAfterTicketChangeByType(gameId, sectionType, bookedSeatsId); // sectionCode 캐시 처리
        handleAfterTicketChangeBySeat(gameId, sectionId, bookedSeatsId);
    }

    @Scheduled(cron = "0 0 0 * * *") // 매일 자정
    public void clearAllGameCaches() {
        Cache cache = cacheManager.getCache("gamesByCondition");
        if (cache != null) {
            cache.clear();
            log.info("🧹 gamesByCondition 캐시 전체 삭제 완료 (자정 정기 삭제)");
        }
    }

    private List<SectionTypeSeatCountResponse> calculateSeatCountBySectionType(Long gameId) {
        // 좌석 ID 별 타입 정보 조회
        List<SectionTypeMapping> seats = gameRepository.findSeatsId(gameId);

        // 예약 된 좌석 ID 조회
        Set<Long> bookedSeatsId = reservationClient.getBookedSeatsId(gameId);

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

    private List<SectionSeatCountResponse> calculateSeatCountBySection(Long gameId, String type) {
        List<SectionCodeMapping> seats = gameRepository.findSeatsIdBySectionType(gameId, type);
        Set<Long> bookedSeatsId = reservationClient.getBookedSeatsId(gameId);

        Map<String, Long> remainingCount = new LinkedHashMap<>();

        for (SectionCodeMapping seat : seats) {
            String code = seat.getSectionCode();
            remainingCount.putIfAbsent(code, 0L);
            if (!bookedSeatsId.contains(seat.getSeatId())) {
                remainingCount.put(code, remainingCount.get(code) + 1);
            }
        }

        return remainingCount.entrySet()
                .stream()
                .map(entry -> new SectionSeatCountResponse(entry.getKey(), entry.getValue()))
                .toList();
    }

    private List<SeatGetResponse> seatInfo(Long gameId, Long sectionId) {
        // 해당 구역의 좌석 ID 조회
        List<Seat> seats = seatService.getSeatsBySectionId(sectionId);
        // 예매된 좌석 ID 조회
        Set<Long> bookedSeatsId = reservationClient.getBookedSeatsId(gameId);

        // 계산 로직
        return seats.stream()
                .map(seat -> SeatGetResponse.of(seat, bookedSeatsId.contains(seat.getId())))
                .toList();
    }

    private List<SectionTypeSeatCountResponse> calculateSeatCountBySectionType(Long gameId, Set<Long> bookedSeatsId) {
        // 좌석 ID 별 타입 정보 조회
        List<SectionTypeMapping> seats = gameRepository.findSeatsId(gameId);

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

    private List<SectionSeatCountResponse> calculateSeatCountBySection(Long gameId, String type, Set<Long> bookedSeatsId) {
        List<SectionCodeMapping> seats = gameRepository.findSeatsIdBySectionType(gameId, type);

        Map<String, Long> remainingCount = new LinkedHashMap<>();

        for (SectionCodeMapping seat : seats) {
            String code = seat.getSectionCode();
            remainingCount.putIfAbsent(code, 0L);
            if (!bookedSeatsId.contains(seat.getSeatId())) {
                remainingCount.put(code, remainingCount.get(code) + 1);
            }
        }

        return remainingCount.entrySet()
                .stream()
                .map(entry -> new SectionSeatCountResponse(entry.getKey(), entry.getValue()))
                .toList();
    }

    private List<SeatGetResponse> seatInfo(Long gameId, Long sectionId, Set<Long> bookedSeatsId) {
        // 해당 구역의 좌석 ID 조회
        List<Seat> seats = seatService.getSeatsBySectionId(sectionId);

        // 계산 로직
        return seats.stream()
                .map(seat -> SeatGetResponse.of(seat, bookedSeatsId.contains(seat.getId())))
                .toList();
    }
}
