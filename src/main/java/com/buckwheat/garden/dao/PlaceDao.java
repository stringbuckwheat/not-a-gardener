package com.buckwheat.garden.dao;

import com.buckwheat.garden.data.dto.PlaceDto;
import com.buckwheat.garden.data.entity.Place;

import java.util.List;

public interface PlaceDao {
    List<Place> getPlacesByGardenerId(Long gardenerId);
    Place getPlaceWithPlants(Long placeId, Long gardenerId);
    Place save(Long gardenerId, PlaceDto.Basic placeRequest);
    Place update(PlaceDto.Basic placeRequest, Long gardenerId);
    void deleteBy(Long placeId, Long gardenerId);
}
