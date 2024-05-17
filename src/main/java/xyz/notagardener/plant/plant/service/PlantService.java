package xyz.notagardener.plant.plant.service;

import xyz.notagardener.place.dto.ModifyPlace;
import xyz.notagardener.place.dto.PlaceDto;
import xyz.notagardener.plant.garden.dto.GardenResponse;
import xyz.notagardener.plant.garden.dto.PlantResponse;
import xyz.notagardener.plant.plant.dto.PlantRequest;

import java.util.List;

public interface PlantService {
    List<PlantResponse> getAll(Long gardenerId);

    PlantResponse getDetail(Long plantId, Long gardenerId);

    GardenResponse add(Long gardenerId, PlantRequest plantRequest);

    GardenResponse update(Long gardenerId, PlantRequest plantRequest);

    PlaceDto updatePlace(ModifyPlace modifyPlantPlace, Long gardenerId);

    void delete(Long plantId, Long gardenerId);
}