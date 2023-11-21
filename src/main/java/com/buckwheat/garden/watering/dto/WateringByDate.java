package com.buckwheat.garden.watering.dto;

import com.buckwheat.garden.data.entity.Chemical;
import com.buckwheat.garden.data.entity.Plant;
import com.buckwheat.garden.data.entity.Watering;
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
