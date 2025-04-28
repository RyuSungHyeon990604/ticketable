package com.example.modulegame.domain.stadium.dto.response;


import lombok.Getter;

@Getter
public class SectionTypeMapping {
    private final String sectionType;
    private final Long seatId;

    public SectionTypeMapping(String sectionType, Long seatId) {
        this.sectionType = sectionType;
        this.seatId = seatId;
    }
}
