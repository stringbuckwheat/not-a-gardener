package xyz.notagardener.plant.plant.service;

import xyz.notagardener.place.Place;
import xyz.notagardener.place.dto.ModifyPlace;
import xyz.notagardener.plant.Plant;
import xyz.notagardener.plant.plant.dto.PlantRequest;

public interface PlantCommandService {
    Plant save(Long gardenerId, PlantRequest plantRequest);

    Plant update(PlantRequest plantRequest, Long gardenerId);

    Place updatePlantPlace(ModifyPlace modifyPlantPlace, Long gardenerId);
}
