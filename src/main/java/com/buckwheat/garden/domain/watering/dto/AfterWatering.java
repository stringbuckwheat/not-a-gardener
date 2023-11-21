package com.buckwheat.garden.domain.watering.dto;

import com.buckwheat.garden.domain.plant.Plant;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AfterWatering {
    private Plant plant;
    private WateringMessage wateringMessage;
}
