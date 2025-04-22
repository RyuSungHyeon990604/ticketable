package com.example.modulegame.domain.stadium.dto.request;

import java.util.List;
import lombok.Getter;

@Getter
public class SeatCreateRequest {
    private List<List<String>> colNums;

    private List<List<Boolean>> isBlind;

    public SeatCreateRequest(List<List<String>> colNums, List<List<Boolean>> isBlind) {
        this.colNums = colNums;
        this.isBlind = isBlind;
    }
}
