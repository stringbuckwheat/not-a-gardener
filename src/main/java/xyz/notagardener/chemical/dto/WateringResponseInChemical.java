package xyz.notagardener.chemical.dto;

import xyz.notagardener.watering.Watering;
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

    public WateringResponseInChemical(Watering watering) {
        this.id = watering.getWateringId();
        this.plantId = watering.getPlant().getPlantId();
        this.plantName = watering.getPlant().getName();
        this.placeId = watering.getPlant().getPlace().getPlaceId();
        this.placeName = watering.getPlant().getPlace().getName();
        this.wateringDate = watering.getWateringDate();
    }
}