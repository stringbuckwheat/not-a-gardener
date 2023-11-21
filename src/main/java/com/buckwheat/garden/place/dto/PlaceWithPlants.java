package com.buckwheat.garden.place.dto;

import com.buckwheat.garden.plant.plant.PlantInPlace;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class PlaceWithPlants {
    private PlaceDto place;
    private List<PlantInPlace> plantList;
}
