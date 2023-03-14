package com.buckwheat.garden.service.impl;

import com.buckwheat.garden.data.dto.*;
import com.buckwheat.garden.data.entity.Member;
import com.buckwheat.garden.data.entity.Place;
import com.buckwheat.garden.data.entity.Plant;
import com.buckwheat.garden.repository.PlaceRepository;
import com.buckwheat.garden.repository.PlantRepository;
import com.buckwheat.garden.service.PlantService;
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

    @Override
    public PlantDto.PlantResponse getOnePlant(int plantNo) {
        // @EntityGraph
        Plant plant = plantRepository.findByPlantNo(plantNo).orElseThrow(NoSuchElementException::new);

        // DTO로 변환
        return PlantDto.PlantResponse.from(plant);
    }

    @Override
    public List<PlantDto.PlantResponse> getPlantList(int memberNo) {
        log.debug("getPlantList");
        List<PlantDto.PlantResponse> plantList = new ArrayList<>();

        // @EntityGraph 메소드
        for(Plant p : plantRepository.findByMember_MemberNoOrderByCreateDateDesc(memberNo)){
            plantList.add(PlantDto.PlantResponse.from(p));
        }

        return plantList;
    }

    @Override
    public void addPlant(PlantDto.PlantRequest plantRequestDto, Member member) {
        Place place = placeRepository.findById(plantRequestDto.getPlaceNo()).orElseThrow(NoSuchElementException::new);
        plantRepository.save(plantRequestDto.toEntityWithMemberAndPlace(member, place));
    }

    @Override
    public PlantDto.PlantResponse modifyPlant(PlantDto.PlantRequest plantRequest, Member member) {
        Place place = placeRepository.findById(plantRequest.getPlaceNo()).orElseThrow(NoSuchElementException::new);
        Plant updatedPlant = plantRequest.toEntityWithMemberAndPlace(member, place);

        plantRepository.save(updatedPlant);
        log.debug("PlantDto.PlantResponse.from(updatedPlace): {}", PlantDto.PlantResponse.from(updatedPlant));

        return PlantDto.PlantResponse.from(updatedPlant);
    }

    @Override
    public PlantDto.PlantResponse postponeAverageWateringPeriod(int plantNo) {
        Plant plant = plantRepository.findByPlantNo(plantNo).orElseThrow(NoSuchElementException::new);

        // 물주기 하루 미룸!
        Plant updatedPlant = plantRepository.save(plant.updateAverageWateringPeriod(plant.getAverageWateringPeriod() + 1));

        return PlantDto.PlantResponse.from(updatedPlant);
    }

    @Override
    public void modifyPlantPlace(ModifyPlantPlaceDto modifyPlantPlaceDto) {
        Place place = placeRepository.findById(modifyPlantPlaceDto.getPlaceNo()).orElseThrow(NoSuchElementException::new);

        for(int plantNo : modifyPlantPlaceDto.getPlantList()){
            Plant plant = plantRepository.findById(plantNo).orElseThrow(NoSuchElementException::new);

            plant.updatePlace(place);
            log.debug("place update: {}", plant);
            plantRepository.save(plant);
        }
    }

    @Override
    public void deletePlantByPlantNo(int plantNo) {
        plantRepository.deleteById(plantNo);
    }
}
