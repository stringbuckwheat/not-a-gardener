package com.buckwheat.garden.dao;

import com.buckwheat.garden.data.dto.PlaceDto;
import com.buckwheat.garden.data.dto.PlantDto;
import com.buckwheat.garden.data.entity.Place;
import com.buckwheat.garden.data.entity.Plant;

import java.util.List;

public interface PlantDao {
    List<Plant> getPlantListByMemberId(Long memberId);
    Plant getPlantWithPlaceAndWatering(Long plantId);
    List<Plant> getPlantsForGarden(Long memberId);
    Plant save(Long memberId, PlantDto.Request plantRequest);
    Plant update(PlantDto.Request plantRequest);
    Plant update(Plant plant);
    Place updatePlantPlace(PlaceDto.ModifyPlantPlace modifyPlantPlaceDto);
    void updateConditionDate(Plant plant);
    void deleteBy(Long id);
}
