package com.buckwheat.garden.plant;

import com.buckwheat.garden.plant.plant.PlantInPlace;
import com.buckwheat.garden.plant.plant.PlantRequest;

public interface PlacePlantService {
    PlantInPlace addPlantInPlace(Long gardenerId, PlantRequest plantRequest);
}
