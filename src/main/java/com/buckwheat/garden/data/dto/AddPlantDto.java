package com.buckwheat.garden.data.dto;

import com.buckwheat.garden.data.entity.Member;
import com.buckwheat.garden.data.entity.Plant;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AddPlantDto {
    private String plantName;
    private String plantSpecies;
    private int averageWateringPeriod;
    private String username;

    public Plant toEntity(){
        return Plant
                .builder()
                .plantName(plantName)
                .plantSpecies(plantSpecies)
                .averageWateringPeriod(averageWateringPeriod)
                .username(username)
                .createDate(LocalDateTime.now())
                .build();
    }
}
