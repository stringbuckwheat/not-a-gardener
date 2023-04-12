package com.buckwheat.garden.dao;

import com.buckwheat.garden.data.dto.PlaceDto;
import com.buckwheat.garden.data.entity.Place;

import java.util.List;

public interface PlaceDao {
    List<Place> getPlaceListByMemberNo(int memberNo);
    Place getPlaceWithPlantList(int placeNo);
    Place save(PlaceDto.PlaceRequestDto placeRequestDto, int memberNo);

    Place update(PlaceDto.PlaceRequestDto placeRequestDto);

    void delete(int placeNo);
}
