package com.buckwheat.garden.dao;

import com.buckwheat.garden.data.dto.PlaceDto;
import com.buckwheat.garden.data.entity.Place;

import java.util.List;

public interface PlaceDao {
    List<Place> getPlacesByGardenerId(Long gardenerId);
    Place getPlaceWithPlantList(Long placeId);
    Place save(Long gardenerId, PlaceDto.Request placeRequest);

    Place update(PlaceDto.Request placeRequest);

    void deleteBy(Long id);
}
