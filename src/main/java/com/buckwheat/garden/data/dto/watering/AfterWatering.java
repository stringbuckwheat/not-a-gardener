package com.buckwheat.garden.data.dto.watering;

import com.buckwheat.garden.data.dto.plant.PlantResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@AllArgsConstructor
@Builder
@Getter
@ToString
public class AfterWatering {
    private PlantResponse plant;
    private List<WateringForOnePlant> waterings;
    private WateringMessage wateringMsg;

    public static AfterWatering from(WateringMessage wateringMsg, List<WateringForOnePlant> waterings) {
        return AfterWatering.builder()
                .wateringMsg(wateringMsg)
                .waterings(waterings)
                .build();
    }

    public static AfterWatering from(PlantResponse plant, WateringMessage wateringMsg, List<WateringForOnePlant> waterings) {
        return AfterWatering.builder()
                .plant(plant)
                .wateringMsg(wateringMsg)
                .waterings(waterings)
                .build();
    }
}