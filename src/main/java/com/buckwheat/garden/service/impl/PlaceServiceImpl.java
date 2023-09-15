package com.buckwheat.garden.service.impl;

import com.buckwheat.garden.dao.PlaceDao;
import com.buckwheat.garden.data.dto.place.PlaceCard;
import com.buckwheat.garden.data.dto.place.PlaceDto;
import com.buckwheat.garden.data.dto.place.PlaceWithPlants;
import com.buckwheat.garden.data.dto.plant.PlantInPlace;
import com.buckwheat.garden.data.entity.Place;
import com.buckwheat.garden.service.PlaceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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
    public List<PlaceCard> getAll(Long gardenerId) {
        return placeDao.getPlacesByGardenerId(gardenerId).stream()
                .map(PlaceCard::from)
                .collect(Collectors.toList());
    }

    /**
     * 한 장소의 정보
     * @param placeId
     * @param gardenerId
     * @return 해당 장소의 식물 개수를 포함하는 장소 정보
     */
    @Override
    public PlaceWithPlants getDetail(Long placeId, Long gardenerId) {
        Place place = placeDao.getPlaceWithPlants(placeId, gardenerId);

        List<PlantInPlace> plants = place.getPlants().stream()
                .map(PlantInPlace::from)
                .collect(Collectors.toList());

        return new PlaceWithPlants(PlaceDto.from(place), plants);
    }

    @Override
    public PlaceCard add(Long gardenerId, PlaceDto placeRequest) {
        // FK인 Gardener와 createDate로 쓸 LocalDateTime.now()를 포함한 엔티티를 저장
        Place place = placeDao.save(gardenerId, placeRequest);
        return PlaceCard.from(place);
    }

    /**
     * 장소 수정
     * @param placeRequest
     * @return 수정 후 데이터
     */
    @Override
    public PlaceDto modify(PlaceDto placeRequest, Long gardenerId) {
        return PlaceDto.from(placeDao.update(placeRequest, gardenerId));
    }

    /**
     * 장소 하나 삭제
     * @param placeId
     */
    @Override
    public void delete(Long placeId, Long gardenerId) {
        placeDao.deleteBy(placeId, gardenerId);
    }
}
