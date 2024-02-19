package com.buckwheat.garden.domain.watering.dto;

import com.buckwheat.garden.domain.chemical.Chemical;
import com.buckwheat.garden.domain.plant.Plant;
import com.buckwheat.garden.domain.watering.Watering;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@ToString
public class WateringByDate {
    @Schema(description = "물주기 id", example = "1")
    private Long id;

    @Schema(description = "식물 id", example = "1")
    private Long plantId;

    @Schema(description = "식물 이름", example = "옥시카르디움 브라질")
    private String plantName;

    @Schema(description = "약품 id")
    private Long chemicalId;

    @Schema(description = "약품 이름")
    private String chemicalName;

    @Schema(description = "물 준 날짜", example = "2024-02-02")
    private LocalDate wateringDate;

    public static WateringByDate from(Watering watering) {
        return WateringByDate.from(watering, watering.getPlant(), watering.getChemical());
    }

    public static WateringByDate from(Watering watering, Plant plant, Chemical chemical) {
        if (chemical == null) {
            return WateringByDate.builder()
                    .id(watering.getWateringId())
                    .plantId(plant.getPlantId())
                    .plantName(plant.getName())
                    .wateringDate(watering.getWateringDate())
                    .build();
        }

        return WateringByDate.builder()
                .id(watering.getWateringId())
                .plantId(plant.getPlantId())
                .plantName(plant.getName())
                .chemicalId(chemical.getChemicalId())
                .chemicalName(chemical.getName())
                .wateringDate(watering.getWateringDate())
                .build();
    }
}
