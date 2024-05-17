package xyz.notagardener.plant.place;

import xyz.notagardener.plant.plant.dto.PlantInPlace;
import xyz.notagardener.plant.plant.dto.PlantRequest;

public interface PlacePlantService {
    PlantInPlace addPlantInPlace(Long gardenerId, PlantRequest plantRequest);
}
