package com.buckwheat.garden.domain.watering.dto;

import com.buckwheat.garden.domain.chemical.Chemical;
import com.buckwheat.garden.domain.plant.Plant;
import com.buckwheat.garden.domain.watering.Watering;
import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@ToString
public class WateringByDate {
    private Long id;
    private Long plantId;
    private String plantName;
    private Long chemicalId;
    private String chemicalName;
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
