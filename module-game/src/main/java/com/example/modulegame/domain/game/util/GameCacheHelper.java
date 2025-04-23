package com.example.modulegame.domain.game.util;


import java.time.LocalDateTime;

import com.example.modulecommon.exception.ErrorCode;
import com.example.modulegame.domain.game.entity.Game;
import com.example.modulegame.domain.game.repository.GameRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import com.example.modulecommon.exception.ServerException;

@Component
@RequiredArgsConstructor
public class GameCacheHelper {
    private final GameRepository gameRepository;

    // 캐시 무효화 전략
    public boolean isEvictStrategy(String key) {
        String[] parts = key.split(":");
        Long gameId = Long.parseLong(parts[0]);
        Game game = gameRepository.findById(gameId).orElseThrow(() -> new ServerException(ErrorCode.GAME_NOT_FOUND));
        LocalDateTime now = LocalDateTime.now();
        return now.isBefore(game.getTicketingStartTime().plusMinutes(30));
    }
}
