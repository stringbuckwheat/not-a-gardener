package com.buckwheat.garden.domain.plant.dto.plant;

import com.buckwheat.garden.domain.plant.Plant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@AllArgsConstructor
@Builder
@Getter
public class PlantInPlace {
    private Long id;
    private String name;
    private String species;
    private int recentWateringPeriod;
    private String medium;
    private LocalDate createDate;

    public static PlantInPlace from(Plant plant) {
        return PlantInPlace.builder()
                .id(plant.getPlantId())
                .name(plant.getName())
                .species(plant.getSpecies())
                .recentWateringPeriod(plant.getRecentWateringPeriod())
                .medium(plant.getMedium())
                .createDate(LocalDate.from(plant.getCreateDate()))
                .build();
    }
}