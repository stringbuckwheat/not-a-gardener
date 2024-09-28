package xyz.notagardener.watering.watering.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import xyz.notagardener.plant.model.Plant;

@AllArgsConstructor
@Getter
public class AfterWatering {
    private Plant plant;
    private WateringMessage wateringMessage;
}
