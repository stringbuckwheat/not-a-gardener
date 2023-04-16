package com.buckwheat.garden.service;

import com.buckwheat.garden.data.dto.PlaceDto;
import com.buckwheat.garden.data.entity.Member;

import java.util.List;

public interface PlaceService {
    List<PlaceDto.Card> getPlaceList(int memberNo);

    PlaceDto.WithPlantList getPlace(int placeNo);

    PlaceDto.Card addPlace(PlaceDto.Request placeRequestDto, Member member);

    PlaceDto.Response modifyPlace(PlaceDto.Request placeRequestDto, Member member);

    void deletePlace(int placeNo);
}
