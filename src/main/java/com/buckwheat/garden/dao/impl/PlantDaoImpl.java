package com.buckwheat.garden.dao.impl;

import com.buckwheat.garden.dao.PlantDao;
import com.buckwheat.garden.data.dto.PlaceDto;
import com.buckwheat.garden.data.dto.PlantDto;
import com.buckwheat.garden.data.entity.Member;
import com.buckwheat.garden.data.entity.Place;
import com.buckwheat.garden.data.entity.Plant;
import com.buckwheat.garden.repository.MemberRepository;
import com.buckwheat.garden.repository.PlaceRepository;
import com.buckwheat.garden.repository.PlantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.NoSuchElementException;

@Repository
@RequiredArgsConstructor
@Slf4j
public class PlantDaoImpl implements PlantDao {
    private final PlantRepository plantRepository;
    private final PlaceRepository placeRepository;
    private final MemberRepository memberRepository;

    @Override
    public List<Plant> getPlantListByMemberNo(int memberNo){
        return plantRepository.findByMember_MemberNoOrderByCreateDateDesc(memberNo);
    }

    @Override
    public Plant getPlantWithPlaceAndWatering(int plantNo) {
        return plantRepository.findByPlantNo(plantNo).orElseThrow(NoSuchElementException::new);
    }

    @Override
    public List<Plant> getPlantListForGarden(int memberNo){
        return plantRepository.findByMember_MemberNo(memberNo);
    }

    @Override
    public Plant save(PlantDto.Request plantRequestDto, int memberNo) {
        Place place = placeRepository.findByPlaceNo(plantRequestDto.getPlaceNo()).orElseThrow(NoSuchElementException::new);
        Member member = memberRepository.findById(memberNo).orElseThrow(NoSuchElementException::new);

        return plantRepository.save(plantRequestDto.toEntityWith(member, place));
    }

    @Override
    public Plant update(PlantDto.Request plantRequest){
        Place place = placeRepository.findByPlaceNo(plantRequest.getPlaceNo()).orElseThrow(NoSuchElementException::new);
        Plant plant = plantRepository.findByPlantNo(plantRequest.getPlantNo()).orElseThrow(NoSuchElementException::new);

        return plantRepository.save(plant.update(plantRequest, place));
    }

    @Override
    public Plant update(Plant plant){
        return plantRepository.save(plant);
    }

    @Override
    public Place updatePlantPlace(PlaceDto.ModifyPlantPlace modifyPlantPlace){
        Place place = placeRepository.findByPlaceNo(modifyPlantPlace.getPlaceNo()).orElseThrow(NoSuchElementException::new);

        for (int plantNo : modifyPlantPlace.getPlantList()) {
            Plant plant = plantRepository.findById(plantNo).orElseThrow(NoSuchElementException::new);

            plant.updatePlace(place);
            plantRepository.save(plant);
        }

        return place;
    }

    public Plant updateConditionDate(Plant plant){
        plantRepository.save(plant.updateConditionDate());
        return plant;
    }

    @Override
    public void deletePlantByPlantNo(int plantNo){
        plantRepository.deleteById(plantNo);
    }
}
