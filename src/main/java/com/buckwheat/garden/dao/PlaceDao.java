package com.buckwheat.garden.dao;

import com.buckwheat.garden.data.dto.place.PlaceDto;
import com.buckwheat.garden.data.entity.Place;
import com.buckwheat.garden.data.entity.Plant;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PlaceDao {
    List<Place> getPlacesByGardenerId(Long gardenerId);
    Place getPlaceWithPlants(Long placeId, Long gardenerId);
    int countPlantsInPlace(Long placeId);
    List<Plant> getPlantsInPlace(Long placeId, Pageable pageable);
    Place save(Long gardenerId, PlaceDto placeRequest);
    Place update(PlaceDto placeRequest, Long gardenerId);
    void deleteBy(Long placeId, Long gardenerId);
}
