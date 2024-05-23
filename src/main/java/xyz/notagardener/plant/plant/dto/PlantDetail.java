package xyz.notagardener.plant.plant.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import xyz.notagardener.plant.garden.dto.PlantResponse;
import xyz.notagardener.watering.plant.dto.WateringForOnePlant;

import java.util.List;

@AllArgsConstructor
@Getter
@ToString
public class PlantDetail {
    PlantResponse plant;
    List<WateringForOnePlant> waterings;
}
