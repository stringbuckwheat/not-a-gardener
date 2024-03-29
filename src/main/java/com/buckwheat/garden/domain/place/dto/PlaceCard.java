package com.buckwheat.garden.domain.place.dto;

import com.buckwheat.garden.domain.place.Place;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * 장소 리스트 카드에서 사용할 정보들
 */
@Getter
@AllArgsConstructor
@Builder
public class PlaceCard {
    @Schema(description = "장소 id", example = "1")
    private Long id;

    @Schema(description = "장소 이름", example = "창가")
    private String name;

    @Schema(description = "식물등 사용 여부", example = "Y")
    private String artificialLight;

    @Schema(description = "장소 타입", example = "실내")
    private String option;

    @Schema(description = "장소에 속한 식물 수", example = "6")
    private int plantListSize;

    @Schema(description = "등록일", example = "2024-02-01")
    private LocalDateTime createDate;

    public static PlaceCard from(Place place) {
        int plantListSize = place.getPlants() != null ? place.getPlants().size() : 0;

        return PlaceCard.builder()
                .id(place.getPlaceId())
                .name(place.getName())
                .artificialLight(place.getArtificialLight())
                .option(place.getOption())
                .plantListSize(plantListSize)
                .createDate(place.getCreateDate())
                .build();
    }
}
