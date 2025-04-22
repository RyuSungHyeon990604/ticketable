package com.example.modulegame.domain.game.dto.request;

import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class GameUpdateRequest {

    private LocalDateTime startTime;

    public GameUpdateRequest(LocalDateTime startTime) {
        this.startTime = startTime;
    }
}
