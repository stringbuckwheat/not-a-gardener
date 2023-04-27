package com.buckwheat.garden.service;

import com.buckwheat.garden.data.dto.PlaceDto;

import java.util.List;

public interface PlaceService {
    List<PlaceDto.Card> getPlacesByMemberId(Long memberId);

    PlaceDto.WithPlantList getPlaceDetail(Long id);

    PlaceDto.Card add(Long memberId, PlaceDto.Request placeRequest);

    PlaceDto.Response modify(PlaceDto.Request placeRequest);

    void delete(Long id);
}
