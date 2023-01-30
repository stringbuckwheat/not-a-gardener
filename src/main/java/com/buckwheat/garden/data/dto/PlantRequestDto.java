package com.buckwheat.garden.data.dto;

import com.buckwheat.garden.data.entity.Plant;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PlantRequestDto {
    private int plantNo;
    private String plantName;
    private String plantSpecies;
    private int averageWateringPeriod;
    private String username;

    public Plant toEntity(){
        return Plant
                .builder()
                .no(plantNo)
                .plantName(plantName)
                .plantSpecies(plantSpecies)
                .averageWateringPeriod(averageWateringPeriod)
                .username(username)
                .createDate(LocalDateTime.now())
                .build();
    }
}
