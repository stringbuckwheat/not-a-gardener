package com.buckwheat.garden.service;

import com.buckwheat.garden.data.dto.PlaceDto;
import com.buckwheat.garden.data.dto.PlantDto;
import com.buckwheat.garden.data.entity.Member;

import java.util.List;

public interface PlaceService {
    List<PlaceDto.PlaceCard> getPlaceList(int memberNo);

    PlaceDto getPlace(int placeNo);

    List<PlantDto.PlantInPlace> getPlantlistInPlace(int placeNo);

    PlaceDto addPlace(PlaceDto placeDto, Member member);

    void modifyPlace(PlaceDto placeDto, Member member);

    void deletePlace(int placeNo);
}
