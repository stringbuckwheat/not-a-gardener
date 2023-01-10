package com.buckwheat.garden.data.dto;

import com.buckwheat.garden.data.entity.Plant;
import com.buckwheat.garden.data.entity.Watering;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class PlantDto {
    private int plantNo;
    private String plantName;
    private String plantSpecies;
    private int averageWateringPeriod;
    // 0        1       2           3
    // 물주기  체크하기    주기 놓침   놔두세요
    private int wateringCode;

    // 0     1
    // 맹물   비료
    private int fertilizingCode;
    private List<WaterDto> waterDtoList;

    public PlantDto(Plant plant){
        this.plantNo = plant.getPlantNo();
        this.plantName = plant.getPlantName();
        this.plantSpecies = plant.getPlantSpecies();
        this.averageWateringPeriod = plant.getAverageWateringPeriod();
    }
}
