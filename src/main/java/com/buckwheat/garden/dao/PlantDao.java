package com.buckwheat.garden.dao;

import com.buckwheat.garden.data.dto.PlaceDto;
import com.buckwheat.garden.data.dto.PlantDto;
import com.buckwheat.garden.data.entity.Place;
import com.buckwheat.garden.data.entity.Plant;

import java.util.List;

public interface PlantDao {
    List<Plant> getPlantListByMemberNo(int memberNo);

    Plant getPlantWithPlaceAndWatering(int plantNo);

    Plant save(PlantDto.PlantRequest plantRequestDto, int memberNo);

    Plant update(PlantDto.PlantRequest plantRequest);

    Place updatePlantPlace(PlaceDto.ModifyPlantPlaceDto modifyPlantPlaceDto);

    void deletePlantByPlantNo(int plantNo);
}
