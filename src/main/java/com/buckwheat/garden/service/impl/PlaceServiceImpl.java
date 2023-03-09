package com.buckwheat.garden.service.impl;

import com.buckwheat.garden.data.dto.PlaceDto;
import com.buckwheat.garden.data.dto.PlantDto;
import com.buckwheat.garden.data.entity.Member;
import com.buckwheat.garden.data.entity.Place;
import com.buckwheat.garden.data.entity.Plant;
import com.buckwheat.garden.repository.PlaceRepository;
import com.buckwheat.garden.service.PlaceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlaceServiceImpl implements PlaceService {
    private final PlaceRepository placeRepository;

    @Override
    public List<PlaceDto.PlaceCard> getPlaceList(int memberNo) {
        List<PlaceDto.PlaceCard> list = new ArrayList<>();

        // @EntityGraph로 plantList를 join한 메소드
        for(Place place : placeRepository.findByMember_memberNo(memberNo)){
            list.add(PlaceDto.PlaceCard.from(place));
        }

        return list;
    }

    @Override
    public PlaceDto.PlaceResponseDto getPlace(int placeNo) {
        // @EntityGraph로 plantList를 join한 메소드
        Place place = placeRepository.findById(placeNo).orElseThrow(NoSuchElementException::new);
        return PlaceDto.PlaceResponseDto.from(place);
    }

    @Override
    public List<PlantDto.PlantInPlace> getPlantlistInPlace(int placeNo) {
        Place place = placeRepository.findById(placeNo).orElseThrow(NoSuchElementException::new);

        List<PlantDto.PlantInPlace> plantList = new ArrayList<>();

        for(Plant plant : place.getPlantList()){
            plantList.add(PlantDto.PlantInPlace.from(plant));
        }

        return plantList;
    }

    @Override
    public PlaceDto.PlaceResponseDto addPlace(PlaceDto.PlaceRequestDto placeRequestDto, Member member) {
        Place place = placeRepository.save(placeRequestDto.toEntityWithMember(member));
        return PlaceDto.PlaceResponseDto.from(place);
    }

    @Override
    public PlaceDto.PlaceResponseDto modifyPlace(PlaceDto.PlaceRequestDto placeRequestDto, Member member) {
        // @EntityGraph 없는 메소드
        Place place = placeRepository.findByPlaceNo(placeRequestDto.getPlaceNo()).orElseThrow(NoSuchElementException::new);
        Place updatedPlace = placeRepository.save(
                place.update(
                        placeRequestDto.getPlaceName(),
                        placeRequestDto.getOption(),
                        placeRequestDto.getArtificialLight()
                )
        );

        return PlaceDto.PlaceResponseDto.from(updatedPlace);
    }

    @Override
    public void deletePlace(int placeNo) {
        placeRepository.deleteById(placeNo);
    }
}
