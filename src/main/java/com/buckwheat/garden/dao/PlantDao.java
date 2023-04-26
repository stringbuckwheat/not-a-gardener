package com.buckwheat.garden.dao;

import com.buckwheat.garden.data.dto.PlaceDto;
import com.buckwheat.garden.data.dto.PlantDto;
import com.buckwheat.garden.data.entity.Place;
import com.buckwheat.garden.data.entity.Plant;

import java.util.List;

public interface PlantDao {
    List<Plant> getPlantListByMemberNo(int memberNo);
    Plant getPlantWithPlaceAndWatering(int plantNo);
    List<Plant> getPlantListForGarden(int memberNo);
    Plant save(PlantDto.Request plantRequest, int memberNo);
    Plant update(PlantDto.Request plantRequest);
    Plant update(Plant plant);
    Place updatePlantPlace(PlaceDto.ModifyPlantPlace modifyPlantPlaceDto);
    Plant updateConditionDate(Plant plant);
    void deletePlantByPlantNo(int plantNo);
}
