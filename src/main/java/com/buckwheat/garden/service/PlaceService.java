package com.buckwheat.garden.service;

import com.buckwheat.garden.data.dto.place.PlaceCard;
import com.buckwheat.garden.data.dto.place.PlaceDto;
import com.buckwheat.garden.data.dto.place.PlaceWithPlants;

import java.util.List;

public interface PlaceService {
    List<PlaceCard> getAll(Long gardenerId);

    PlaceWithPlants getDetail(Long placeId, Long gardenerId);

    PlaceCard add(Long gardenerId, PlaceDto placeRequest);

    PlaceDto modify(PlaceDto placeRequest, Long gardenerId);

    void delete(Long placeId, Long gardenerId);
}
