package com.buckwheat.garden.service;

import com.buckwheat.garden.data.dto.PlaceDto;

import java.util.List;

public interface PlaceService {
    List<PlaceDto.Card> getAll(Long gardenerId);

    PlaceDto.WithPlants getDetail(Long placeId, Long gardenerId);

    PlaceDto.Card add(Long gardenerId, PlaceDto.Request placeRequest);

    PlaceDto.Response modify(PlaceDto.Request placeRequest, Long gardenerId);

    void delete(Long placeId, Long gardenerId);
}
