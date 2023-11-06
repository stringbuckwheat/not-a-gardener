package com.buckwheat.garden.repository.command;

import com.buckwheat.garden.data.dto.place.ModifyPlace;
import com.buckwheat.garden.data.dto.plant.PlantRequest;
import com.buckwheat.garden.data.entity.Place;
import com.buckwheat.garden.data.entity.Plant;

public interface PlantCommandRepository {
    Plant save(Long gardenerId, PlantRequest plantRequest);
    Plant update(PlantRequest plantRequest, Long gardenerId);
    Place updatePlantPlace(ModifyPlace modifyPlantPlaceDto, Long gardenerId);
    void deleteBy(Long id, Long gardenerId);
}
