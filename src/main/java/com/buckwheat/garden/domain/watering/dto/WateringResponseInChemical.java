package com.buckwheat.garden.domain.watering.dto;

import com.buckwheat.garden.domain.watering.Watering;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;

@AllArgsConstructor
@Builder
@Getter
@ToString
public class WateringResponseInChemical {
    @Schema(description = "물 주기 id", example = "1")
    private Long id;

    @Schema(description = "식물 id", example = "3")
    private Long plantId;

    @Schema(description = "식물 이름", example = "아디안텀")
    private String plantName;

    @Schema(description = "장소 id", example = "4")
    private Long placeId;

    @Schema(description = "장소 이름", example = "책상 위")
    private String placeName;

    @Schema(description = "물 준 날짜", example = "2024-01-30")
    private LocalDate wateringDate;

    public static WateringResponseInChemical from(Watering watering) {
        return WateringResponseInChemical.builder()
                .id(watering.getWateringId())
                .plantId(watering.getPlant().getPlantId())
                .plantName(watering.getPlant().getName())
                .placeId(watering.getPlant().getPlace().getPlaceId())
                .placeName(watering.getPlant().getPlace().getName())
                .wateringDate(watering.getWateringDate())
                .build();
    }
}