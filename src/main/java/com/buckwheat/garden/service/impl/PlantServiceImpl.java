package com.buckwheat.garden.service.impl;

import com.buckwheat.garden.data.dto.GardenDto;
import com.buckwheat.garden.data.dto.PlaceDto;
import com.buckwheat.garden.data.dto.PlantDto;
import com.buckwheat.garden.data.entity.Member;
import com.buckwheat.garden.data.entity.Place;
import com.buckwheat.garden.data.entity.Plant;
import com.buckwheat.garden.repository.PlaceRepository;
import com.buckwheat.garden.repository.PlantRepository;
import com.buckwheat.garden.service.PlantService;
import com.buckwheat.garden.util.GardenUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Slf4j
@RequiredArgsConstructor
public class PlantServiceImpl implements PlantService {
    private final PlantRepository plantRepository;
    private final PlaceRepository placeRepository;
    private final GardenUtil gardenUtil;

    /**
     * 하나의 장소 정보 반환
     * @param plantNo
     * @return
     */
    @Override
    public PlantDto.PlantResponse getOnePlant(int plantNo) {
        // @EntityGraph
        Plant plant = plantRepository.findByPlantNo(plantNo).orElseThrow(NoSuchElementException::new);

        // DTO로 변환
        return PlantDto.PlantResponse.from(plant);
    }

    @Override
    public List<PlantDto.PlantResponse> getPlantList(int memberNo) {
        List<PlantDto.PlantResponse> plantList = new ArrayList<>();

        // @EntityGraph 메소드
        for(Plant p : plantRepository.findByMember_MemberNoOrderByCreateDateDesc(memberNo)){
            plantList.add(PlantDto.PlantResponse.from(p));
        }

        return plantList;
    }

    @Override
    public PlantDto.PlantInPlace addPlant(PlantDto.PlantRequest plantRequestDto, Member member) {
        Place place = placeRepository.findByPlaceNo(plantRequestDto.getPlaceNo()).orElseThrow(NoSuchElementException::new);
        Plant plant = plantRepository.save(plantRequestDto.toEntityWith(member, place));

        return PlantDto.PlantInPlace.from(plant);
    }

    @Override
    public GardenDto.GardenResponse modifyPlant(PlantDto.PlantRequest plantRequest, Member member) {
        Place place = placeRepository.findByPlaceNo(plantRequest.getPlaceNo()).orElseThrow(NoSuchElementException::new);
        Plant plant = plantRepository.findByPlantNo(plantRequest.getPlantNo()).orElseThrow(NoSuchElementException::new);

        Plant updatedPlant = plant.update(plantRequest, place);
        plantRepository.save(updatedPlant);

        // GardenDto를 돌려줘야 함
        PlantDto.PlantResponse plantResponse = PlantDto.PlantResponse.from(updatedPlant);
        GardenDto.GardenDetail gardenDetail = gardenUtil.getGardenDetail(member.getMemberNo(), updatedPlant);

        return new GardenDto.GardenResponse(plantResponse, gardenDetail);
    }

    @Override
    public PlantDto.PlantResponse postponeAverageWateringPeriod(int plantNo) {
        Plant plant = plantRepository.findByPlantNo(plantNo).orElseThrow(NoSuchElementException::new);

        // 물주기 하루 미룸!
        Plant updatedPlant = plantRepository.save(plant.updateAverageWateringPeriod(plant.getAverageWateringPeriod() + 1));

        return PlantDto.PlantResponse.from(updatedPlant);
    }

    @Override
    public PlaceDto.PlaceResponseDto modifyPlantPlace(PlaceDto.ModifyPlantPlaceDto modifyPlantPlaceDto) {
        Place place = placeRepository.findByPlaceNo(modifyPlantPlaceDto.getPlaceNo()).orElseThrow(NoSuchElementException::new);

        for(int plantNo : modifyPlantPlaceDto.getPlantList()){
            Plant plant = plantRepository.findById(plantNo).orElseThrow(NoSuchElementException::new);

            plant.updatePlace(place);
            plantRepository.save(plant);
        }

        return PlaceDto.PlaceResponseDto.from(place);
    }

    @Override
    public void deletePlantByPlantNo(int plantNo) {
        plantRepository.deleteById(plantNo);
    }
}
