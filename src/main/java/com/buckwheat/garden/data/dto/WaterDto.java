package com.buckwheat.garden.data.dto;

import com.buckwheat.garden.data.entity.Member;
import com.buckwheat.garden.data.entity.Watering;
import lombok.Data;

import java.time.LocalDate;

@Data
public class WaterDto {
    private int plantNo;
    private String fertilized;

    public Watering toEntity(){
        return Watering
                .builder()
                .plantNo(plantNo)
                .fertilized(fertilized)
                .wateringDate(LocalDate.now())
                .build();
    }
}
