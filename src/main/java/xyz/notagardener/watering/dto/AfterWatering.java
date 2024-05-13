package xyz.notagardener.domain.watering.dto;

import xyz.notagardener.domain.plant.Plant;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AfterWatering {
    private Plant plant;
    private WateringMessage wateringMessage;
}
