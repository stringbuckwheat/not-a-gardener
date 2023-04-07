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

    /**
     * 전체 장소 리스트
     * @param memberNo int
     * @return 각 장소의 식물 개수를 포함하는 장소 정보 리스트
     */
    @Override
    public List<PlaceDto.PlaceCard> getPlaceList(int memberNo) {
        List<PlaceDto.PlaceCard> list = new ArrayList<>();

        // @EntityGraph로 plantList를 join한 메소드
        for(Place place : placeRepository.findByMember_memberNoOrderByCreateDate(memberNo)){
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
        // @EntityGraph로 plantList를 join한 메소드
        Place place = placeRepository.findByPlaceNo(placeNo).orElseThrow(NoSuchElementException::new);

        List<PlantDto.PlantInPlace> plantList = new ArrayList<>();

        for(Plant plant : place.getPlantList()){
            plantList.add(PlantDto.PlantInPlace.from(plant));
        }

        PlaceDto.PlaceResponseDto placeResponseDto = PlaceDto.PlaceResponseDto.from(place);

        return new PlaceDto.WithPlantList(placeResponseDto, plantList);
    }

    /**
     * 해당 장소에 속한 식물의 상세 정보 리스트
     * @param placeNo int
     * @return 해당 장소에 속한 식물의 상세 정보 리스트
     */
    @Override
    public List<PlantDto.PlantInPlace> getPlantlistInPlace(int placeNo) {
        Place place = placeRepository.findByPlaceNo(placeNo).orElseThrow(NoSuchElementException::new);

        List<PlantDto.PlantInPlace> plantList = new ArrayList<>();

        for(Plant plant : place.getPlantList()){
            plantList.add(PlantDto.PlantInPlace.from(plant));
        }

        return plantList;
    }

    /**
     * 장소 추가
     * @param placeRequestDto
     * @param member
     * @return
     */
    @Override
    public PlaceDto.PlaceResponseDto addPlace(PlaceDto.PlaceRequestDto placeRequestDto, Member member) {
        // FK인 Member와 createDate로 쓸 LocalDateTime.now()를 포함한 엔티티를 저장
        Place place = placeRepository.save(placeRequestDto.toEntityWithMember(member));
        return PlaceDto.PlaceResponseDto.from(place);
    }

    /**
     * 장소 수정
     * @param placeRequestDto
     * @param member
     * @return 수정 후 데이터
     */
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

    /**
     * 장소 하나 삭제
     * @param placeNo
     */
    @Override
    public void deletePlace(int placeNo) {
        placeRepository.deleteById(placeNo);
    }
}
