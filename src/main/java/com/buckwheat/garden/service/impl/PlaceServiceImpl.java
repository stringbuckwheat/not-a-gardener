package com.buckwheat.garden.service.impl;

import com.buckwheat.garden.dao.PlaceDao;
import com.buckwheat.garden.data.dto.PlaceDto;
import com.buckwheat.garden.data.dto.PlantDto;
import com.buckwheat.garden.data.entity.Place;
import com.buckwheat.garden.data.entity.Plant;
import com.buckwheat.garden.service.PlaceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlaceServiceImpl implements PlaceService {
    private final PlaceDao placeDao;


    /**
     * 전체 장소 리스트
     * @param gardenerId int
     * @return 각 장소의 식물 개수를 포함하는 장소 정보 리스트
     */
    @Override
    public List<PlaceDto.Card> getPlacesByGardenerId(Long gardenerId) {
        List<PlaceDto.Card> list = new ArrayList<>();

        for(Place place : placeDao.getPlacesByGardenerId(gardenerId)){
            list.add(PlaceDto.Card.from(place));
        }

        return list;
    }

    /**
     * 한 장소의 정보
     * @param id
     * @return 해당 장소의 식물 개수를 포함하는 장소 정보
     */
    @Override
    public PlaceDto.WithPlantList getPlaceDetail(Long id) {
        Place place = placeDao.getPlaceWithPlantList(id);

        List<PlantDto.PlantInPlace> plantList = new ArrayList<>();

        for(Plant plant : place.getPlants()){
            plantList.add(PlantDto.PlantInPlace.from(plant));
        }

        PlaceDto.Response placeResponseDto = PlaceDto.Response.from(place);

        return new PlaceDto.WithPlantList(placeResponseDto, plantList);
    }

    @Override
    public PlaceDto.Card add(Long gardenerId, PlaceDto.Request placeRequest) {
        // FK인 Gardener와 createDate로 쓸 LocalDateTime.now()를 포함한 엔티티를 저장
        Place place = placeDao.save(gardenerId, placeRequest);
        return PlaceDto.Card.fromNew(place);
    }

    /**
     * 장소 수정
     * @param placeRequest
     * @return 수정 후 데이터
     */
    @Override
    public PlaceDto.Response modify(PlaceDto.Request placeRequest) {
        return PlaceDto.Response.from(placeDao.update(placeRequest));
    }

    /**
     * 장소 하나 삭제
     * @param id
     */
    @Override
    public void delete(Long id) {
        placeDao.deleteBy(id);
    }
}
