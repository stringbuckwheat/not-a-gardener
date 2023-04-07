package com.buckwheat.garden.service;

import com.buckwheat.garden.data.dto.PlaceDto;
import com.buckwheat.garden.data.dto.PlantDto;
import com.buckwheat.garden.data.entity.Member;

import java.util.List;

public interface PlaceService {
    List<PlaceDto.PlaceCard> getPlaceList(int memberNo);

    PlaceDto.WithPlantList getPlace(int placeNo);

    List<PlantDto.PlantInPlace> getPlantlistInPlace(int placeNo);

    PlaceDto.PlaceResponseDto addPlace(PlaceDto.PlaceRequestDto placeRequestDto, Member member);

    PlaceDto.PlaceResponseDto modifyPlace(PlaceDto.PlaceRequestDto placeRequestDto, Member member);

    void deletePlace(int placeNo);
}
