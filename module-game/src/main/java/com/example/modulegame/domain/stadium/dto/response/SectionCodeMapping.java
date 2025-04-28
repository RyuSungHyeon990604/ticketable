package com.example.modulegame.domain.stadium.dto.response;


import lombok.Getter;

@Getter
public class SectionCodeMapping {
    private final String sectionCode;
    private final Long seatId;

    public SectionCodeMapping(String sectionCode, Long seatId) {
        this.sectionCode = sectionCode;
        this.seatId = seatId;
    }
}
