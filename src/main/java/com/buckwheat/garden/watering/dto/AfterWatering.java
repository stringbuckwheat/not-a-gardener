package com.buckwheat.garden.watering.dto;

import com.buckwheat.garden.data.entity.Plant;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AfterWatering {
    private Plant plant;
    private WateringMessage wateringMessage;
}
