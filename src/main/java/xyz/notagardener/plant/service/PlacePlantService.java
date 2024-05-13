package xyz.notagardener.domain.plant.service;

import xyz.notagardener.domain.plant.dto.plant.PlantInPlace;
import xyz.notagardener.domain.plant.dto.plant.PlantRequest;

public interface PlacePlantService {
    PlantInPlace addPlantInPlace(Long gardenerId, PlantRequest plantRequest);
}
