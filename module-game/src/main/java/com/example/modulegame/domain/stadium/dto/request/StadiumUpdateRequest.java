package com.example.modulegame.domain.stadium.dto.request;

import lombok.Getter;

@Getter
public class StadiumUpdateRequest {
    private String name;
    public StadiumUpdateRequest(String name) {
        this.name = name;
    }
}
