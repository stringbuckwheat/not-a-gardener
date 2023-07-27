package com.buckwheat.garden.service;

import com.buckwheat.garden.data.dto.PlaceDto;

import java.util.List;

public interface PlaceService {
    List<PlaceDto.Card> getAll(Long gardenerId);

    PlaceDto.WithPlants getDetail(Long placeId, Long gardenerId);

    PlaceDto.Card add(Long gardenerId, PlaceDto.Basic placeRequest);

    PlaceDto.Basic modify(PlaceDto.Basic placeRequest, Long gardenerId);

    void delete(Long placeId, Long gardenerId);
}
