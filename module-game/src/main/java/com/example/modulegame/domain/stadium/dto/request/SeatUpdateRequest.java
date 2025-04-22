package com.example.modulegame.domain.stadium.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SeatUpdateRequest {

    private boolean isBlind;

    public SeatUpdateRequest(boolean isBlind) {
        this.isBlind = isBlind;
    }
}
