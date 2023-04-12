package com.buckwheat.garden.service.impl;

import com.buckwheat.garden.dao.PlaceDao;
import com.buckwheat.garden.data.dto.PlaceDto;
import com.buckwheat.garden.data.dto.PlantDto;
import com.buckwheat.garden.data.entity.Member;
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
     * @param memberNo int
     * @return 각 장소의 식물 개수를 포함하는 장소 정보 리스트
     */
    @Override
    public List<PlaceDto.PlaceCard> getPlaceList(int memberNo) {
        List<PlaceDto.PlaceCard> list = new ArrayList<>();

        for(Place place : placeDao.getPlaceListByMemberNo(memberNo)){
            list.add(PlaceDto.PlaceCard.from(place));
        }

        return list;
    }

    /**
     * 한 장소의 정보
     * @param placeNo
     * @return 해당 장소의 식물 개수를 포함하는 장소 정보
     */
    @Override
    public PlaceDto.WithPlantList getPlace(int placeNo) {
        Place place = placeDao.getPlaceWithPlantList(placeNo);

        List<PlantDto.PlantInPlace> plantList = new ArrayList<>();

        for(Plant plant : place.getPlantList()){
            plantList.add(PlantDto.PlantInPlace.from(plant));
        }

        PlaceDto.PlaceResponseDto placeResponseDto = PlaceDto.PlaceResponseDto.from(place);

        return new PlaceDto.WithPlantList(placeResponseDto, plantList);
    }

    /**
     * 장소 추가
     * @param placeRequestDto
     * @param member
     * @return
     */
    @Override
    public PlaceDto.PlaceCard addPlace(PlaceDto.PlaceRequestDto placeRequestDto, Member member) {
        // FK인 Member와 createDate로 쓸 LocalDateTime.now()를 포함한 엔티티를 저장
        Place place = placeDao.save(placeRequestDto, member.getMemberNo());
        return PlaceDto.PlaceCard.fromNew(place);
    }

    /**
     * 장소 수정
     * @param placeRequestDto
     * @param member
     * @return 수정 후 데이터
     */
    @Override
    public PlaceDto.PlaceResponseDto modifyPlace(PlaceDto.PlaceRequestDto placeRequestDto, Member member) {
        return PlaceDto.PlaceResponseDto.from(placeDao.update(placeRequestDto));
    }

    /**
     * 장소 하나 삭제
     * @param placeNo
     */
    @Override
    public void deletePlace(int placeNo) {
        placeDao.delete(placeNo);
    }
}
