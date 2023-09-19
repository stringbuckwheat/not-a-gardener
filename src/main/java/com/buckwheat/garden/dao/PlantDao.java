package com.buckwheat.garden.dao;

import com.buckwheat.garden.data.dto.place.ModifyPlace;
import com.buckwheat.garden.data.dto.plant.PlantRequest;
import com.buckwheat.garden.data.entity.Place;
import com.buckwheat.garden.data.entity.Plant;
import com.buckwheat.garden.data.projection.RawGarden;

import java.util.List;

public interface PlantDao {
    List<RawGarden> getGarden(Long gardenerId);
    List<Plant> getWaitingForWateringList(Long gardenerId);
    List<Plant> getPlantsByGardenerId(Long gardenerId);
    Plant getPlantWithPlaceAndWatering(Long plantId);
    Plant getPlantWithPlantIdAndGardenerId(Long plantId, Long gardenerId);
    List<Plant> getPlantsForGarden(Long gardenerId);
    Plant save(Long gardenerId, PlantRequest plantRequest);
    Plant update(PlantRequest plantRequest, Long gardenerId);
    Plant updateWateringPeriod(Plant plant, int period);
    Place updatePlantPlace(ModifyPlace modifyPlantPlaceDto, Long gardenerId);
    void updateConditionDate(Plant plant);
    void deleteBy(Long id, Long gardenerId);
}
