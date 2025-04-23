package com.example.modulegame.domain.stadium.dto.response;


import java.util.List;

import com.example.modulegame.domain.stadium.entity.Stadium;
import lombok.Getter;

@Getter
public class StadiumGetResponse {
    private final Long id;

    private final String name;

    private final String imagePath;

    private List<SectionTypeSeatCountResponse> sectionSeatCounts;

    public StadiumGetResponse(Long id, String name, String imagePath, List<SectionTypeSeatCountResponse> sectionSeatCounts) {
        this.id = id;
        this.name = name;
        this.imagePath = imagePath;
        this.sectionSeatCounts = sectionSeatCounts;
    }

    public static StadiumGetResponse of(Stadium stadium, List<SectionTypeSeatCountResponse> sectionSeatCounts) {
        return new StadiumGetResponse(
                stadium.getId(),
                stadium.getName(),
                stadium.getImagePath(),
                sectionSeatCounts
        );
    }
}
