package com.buckwheat.garden.dao;

import com.buckwheat.garden.data.dto.PlaceDto;
import com.buckwheat.garden.data.dto.PlantDto;
import com.buckwheat.garden.data.dto.RawGarden;
import com.buckwheat.garden.data.entity.Place;
import com.buckwheat.garden.data.entity.Plant;

import java.util.List;

public interface PlantDao {
    List<RawGarden> getGarden(Long gardenerId);
    List<Plant> getWaitingForWateringList(Long gardenerId);
    List<Plant> getPlantsByGardenerId(Long gardenerId);
    Plant getPlantWithPlaceAndWatering(Long plantId);
    Plant getPlantWithPlantIdAndGardenerId(Long plantId, Long gardenerId);
    List<Plant> getPlantsForGarden(Long gardenerId);
    Plant save(Long gardenerId, PlantDto.Request plantRequest);
    Plant update(PlantDto.Request plantRequest, Long gardenerId);
    Plant update(Plant plant);
    Place updatePlantPlace(PlaceDto.ModifyPlantPlace modifyPlantPlaceDto, Long gardenerId);
    void updateConditionDate(Plant plant);
    void deleteBy(Long id, Long gardenerId);
}
