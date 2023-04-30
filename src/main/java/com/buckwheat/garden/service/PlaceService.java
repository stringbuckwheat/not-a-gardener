package com.buckwheat.garden.service;

import com.buckwheat.garden.data.dto.PlaceDto;

import java.util.List;

public interface PlaceService {
    List<PlaceDto.Card> getPlacesByGardenerId(Long gardenerId);

    PlaceDto.WithPlants getPlaceDetail(Long id);

    PlaceDto.Card add(Long gardenerId, PlaceDto.Request placeRequest);

    PlaceDto.Response modify(PlaceDto.Request placeRequest);

    void delete(Long id);
}
