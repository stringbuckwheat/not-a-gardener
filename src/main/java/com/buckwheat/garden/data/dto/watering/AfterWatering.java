package com.buckwheat.garden.data.dto.watering;

import com.buckwheat.garden.data.entity.Plant;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AfterWatering {
    private Plant plant;
    private WateringMessage wateringMessage;
}
