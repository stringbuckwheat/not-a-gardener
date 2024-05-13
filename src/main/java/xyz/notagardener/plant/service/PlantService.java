package xyz.notagardener.domain.plant.service;

import xyz.notagardener.domain.place.dto.ModifyPlace;
import xyz.notagardener.domain.place.dto.PlaceDto;
import xyz.notagardener.domain.plant.dto.garden.GardenResponse;
import xyz.notagardener.domain.plant.dto.plant.PlantRequest;
import xyz.notagardener.domain.plant.dto.plant.PlantResponse;

import java.util.List;

public interface PlantService {
    List<PlantResponse> getAll(Long gardenerId);

    PlantResponse getDetail(Long plantId, Long gardenerId);

    GardenResponse add(Long gardenerId, PlantRequest plantRequest);

    GardenResponse update(Long gardenerId, PlantRequest plantRequest);

    PlaceDto updatePlace(ModifyPlace modifyPlantPlace, Long gardenerId);

    void delete(Long plantId, Long gardenerId);
}