package com.example.modulegame.domain.stadium.dto.response;


import lombok.Getter;

@Getter
public class SectionTypeSeatCountResponse {
    private final String sectionType;
    private final Long seatsCount;

    public SectionTypeSeatCountResponse(String sectionType, Long seatsCount) {
        this.sectionType = sectionType;
        this.seatsCount = seatsCount;
    }
}
