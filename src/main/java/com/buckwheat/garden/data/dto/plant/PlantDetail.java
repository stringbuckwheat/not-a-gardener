package com.buckwheat.garden.data.dto.plant;

import com.buckwheat.garden.data.dto.watering.WateringForOnePlant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@AllArgsConstructor
@Getter
@ToString
public class PlantDetail{
    PlantResponse plant;
    List<WateringForOnePlant> waterings;
}
