package com.buckwheat.garden.repository.command;

import com.buckwheat.garden.data.dto.place.PlaceDto;
import com.buckwheat.garden.data.entity.Place;

public interface PlaceCommandRepository {
    Place save(Long gardenerId, PlaceDto placeRequest);
    Place update(PlaceDto placeRequest, Long gardenerId);
    void deleteBy(Long placeId, Long gardenerId);
}
