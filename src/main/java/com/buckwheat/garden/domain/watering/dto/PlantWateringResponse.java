package com.buckwheat.garden.domain.watering.dto;

import com.buckwheat.garden.domain.plant.dto.plant.PlantResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@AllArgsConstructor
@Builder
@Getter
@ToString
public class PlantWateringResponse {
    private PlantResponse plant;
    private List<WateringForOnePlant> waterings;
    private WateringMessage wateringMsg;

    public static PlantWateringResponse from(WateringMessage wateringMsg, List<WateringForOnePlant> waterings) {
        return PlantWateringResponse.builder()
                .wateringMsg(wateringMsg)
                .waterings(waterings)
                .build();
    }

    public static PlantWateringResponse from(PlantResponse plant, WateringMessage wateringMsg, List<WateringForOnePlant> waterings) {
        return PlantWateringResponse.builder()
                .plant(plant)
                .wateringMsg(wateringMsg)
                .waterings(waterings)
                .build();
    }
}