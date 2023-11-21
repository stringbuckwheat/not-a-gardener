package com.buckwheat.garden.domain.plant.dto.plant;

import com.buckwheat.garden.domain.watering.dto.WateringForOnePlant;
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
