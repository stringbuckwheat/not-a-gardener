package com.buckwheat.garden.domain.plant.service;

import com.buckwheat.garden.domain.plant.dto.plant.PlantInPlace;
import com.buckwheat.garden.domain.plant.dto.plant.PlantRequest;

public interface PlacePlantService {
    PlantInPlace addPlantInPlace(Long gardenerId, PlantRequest plantRequest);
}
