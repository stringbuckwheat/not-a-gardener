package com.buckwheat.garden.watering.dto;

import com.buckwheat.garden.data.entity.Chemical;
import com.buckwheat.garden.data.entity.Plant;
import com.buckwheat.garden.data.entity.Watering;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@ToString
public class WateringRequest {
    private Long id;
    private Long plantId;
    private Long chemicalId;
    private LocalDate wateringDate;

    public Watering toEntityWithPlantAndChemical(Plant plant, Chemical chemical) {
        return Watering.builder()
                .plant(plant)
                .chemical(chemical)
                .wateringDate(wateringDate)
                .build();
    }

    public Watering toEntityWithPlant(Plant plant) {
        return Watering.builder()
                .plant(plant)
                .wateringDate(wateringDate)
                .build();
    }
}
