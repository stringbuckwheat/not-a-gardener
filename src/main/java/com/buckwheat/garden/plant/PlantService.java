package com.buckwheat.garden.plant;

import com.buckwheat.garden.plant.garden.GardenResponse;
import com.buckwheat.garden.place.dto.ModifyPlace;
import com.buckwheat.garden.place.dto.PlaceDto;
import com.buckwheat.garden.plant.plant.PlantRequest;
import com.buckwheat.garden.plant.plant.PlantResponse;

import java.util.List;

public interface PlantService {
    List<PlantResponse> getAll(Long gardenerId);

    PlantResponse getDetail(Long plantId, Long gardenerId);

    GardenResponse add(Long gardenerId, PlantRequest plantRequest);

    GardenResponse update(Long gardenerId, PlantRequest plantRequest);

    PlaceDto updatePlace(ModifyPlace modifyPlantPlace, Long gardenerId);

    void delete(Long plantId, Long gardenerId);
}