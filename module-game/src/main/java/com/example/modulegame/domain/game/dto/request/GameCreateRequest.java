package com.example.modulegame.domain.game.dto.request;

import com.example.ticketable.domain.game.enums.GameType;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class GameCreateRequest {
    private Long stadiumId;

    private String away;

    private String home;

    private GameType type;

    private Integer point;

    private LocalDateTime startTime;

    public GameCreateRequest(Long stadiumId, String away, String home, GameType type, Integer point, LocalDateTime startTime) {
        this.stadiumId = stadiumId;
        this.away = away;
        this.home = home;
        this.type = type;
        this.point = point;
        this.startTime = startTime;
    }
}
