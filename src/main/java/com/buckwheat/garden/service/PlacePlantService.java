package com.buckwheat.garden.service;

import com.buckwheat.garden.data.dto.plant.PlantInPlace;
import com.buckwheat.garden.data.dto.plant.PlantRequest;

public interface PlacePlantService {
    PlantInPlace addPlantInPlace(Long gardenerId, PlantRequest plantRequest);
}
