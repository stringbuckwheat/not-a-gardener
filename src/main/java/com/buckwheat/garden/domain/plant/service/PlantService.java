package com.buckwheat.garden.domain.plant.service;

import com.buckwheat.garden.domain.place.dto.ModifyPlace;
import com.buckwheat.garden.domain.place.dto.PlaceDto;
import com.buckwheat.garden.domain.plant.dto.garden.GardenResponse;
import com.buckwheat.garden.domain.plant.dto.plant.PlantRequest;
import com.buckwheat.garden.domain.plant.dto.plant.PlantResponse;

import java.util.List;

public interface PlantService {
    List<PlantResponse> getAll(Long gardenerId);

    PlantResponse getDetail(Long plantId, Long gardenerId);

    GardenResponse add(Long gardenerId, PlantRequest plantRequest);

    GardenResponse update(Long gardenerId, PlantRequest plantRequest);

    PlaceDto updatePlace(ModifyPlace modifyPlantPlace, Long gardenerId);

    void delete(Long plantId, Long gardenerId);
}