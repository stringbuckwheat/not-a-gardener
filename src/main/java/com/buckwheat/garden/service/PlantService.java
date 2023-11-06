package com.buckwheat.garden.service;

import com.buckwheat.garden.data.dto.garden.GardenResponse;
import com.buckwheat.garden.data.dto.place.ModifyPlace;
import com.buckwheat.garden.data.dto.place.PlaceDto;
import com.buckwheat.garden.data.dto.plant.PlantRequest;
import com.buckwheat.garden.data.dto.plant.PlantResponse;

import java.util.List;

public interface PlantService {
    List<PlantResponse> getAll(Long gardenerId);

    PlantResponse getDetail(Long plantId, Long gardenerId);

    GardenResponse add(Long gardenerId, PlantRequest plantRequest);

    GardenResponse update(Long gardenerId, PlantRequest plantRequest);

    PlaceDto updatePlace(ModifyPlace modifyPlantPlace, Long gardenerId);

    void delete(Long plantId, Long gardenerId);
}