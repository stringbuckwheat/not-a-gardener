package com.buckwheat.garden.service;

import com.buckwheat.garden.data.dto.PlaceDto;

import java.util.List;

public interface PlaceService {
    List<PlaceDto> getPlaceList(int memberNo);
    PlaceDto getPlace(int placeNo);

    PlaceDto addOrUpdatePlace(PlaceDto placeDto);

    void deletePlace(int placeNo);
}
