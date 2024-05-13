package xyz.notagardener.domain.plant.dto.plant;

import xyz.notagardener.domain.watering.dto.WateringForOnePlant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@AllArgsConstructor
@Getter
@ToString
public class PlantDetail {
    PlantResponse plant;
    List<WateringForOnePlant> waterings;
}
