package com.example.modulegame.domain.stadium.dto.request;

import lombok.Getter;

@Getter
public class SectionUpdateRequest {
    private String type;

    private String code;

    private Integer extraCharge;

    public SectionUpdateRequest(String type, String code, Integer extraCharge) {
        this.type = type;
        this.code = code;
        this.extraCharge = extraCharge;
    }
}
