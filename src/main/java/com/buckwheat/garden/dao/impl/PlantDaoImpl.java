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
    public List<Plant> getPlantListByMemberId(Long memberId){
        return plantRepository.findByMember_MemberIdOrderByCreateDateDesc(memberId);
    }

    @Override
    public Plant getPlantWithPlaceAndWatering(Long id) {
        return plantRepository.findByPlantId(id).orElseThrow(NoSuchElementException::new);
    }

    @Override
    public List<Plant> getPlantsForGarden(Long memberId){
        return plantRepository.findByMember_MemberId(memberId);
    }

    @Override
    public Plant save(Long memberId, PlantDto.Request plantRequest) {
        Place place = placeRepository.findByPlaceId(plantRequest.getPlaceId()).orElseThrow(NoSuchElementException::new);
        Member member = memberRepository.findById(memberId).orElseThrow(NoSuchElementException::new);

        return plantRepository.save(plantRequest.toEntityWith(member, place));
    }

    @Override
    public Plant update(PlantDto.Request plantRequest){
        Place place = placeRepository.findByPlaceId(plantRequest.getPlaceId()).orElseThrow(NoSuchElementException::new);
        Plant plant = plantRepository.findByPlantId(plantRequest.getId()).orElseThrow(NoSuchElementException::new);

        return plantRepository.save(plant.update(plantRequest, place));
    }

    @Override
    public Plant update(Plant plant){
        return plantRepository.save(plant);
    }

    @Override
    public Place updatePlantPlace(PlaceDto.ModifyPlantPlace modifyPlantPlace){
        Place place = placeRepository.findByPlaceId(modifyPlantPlace.getPlaceId()).orElseThrow(NoSuchElementException::new);

        for (Long plantId : modifyPlantPlace.getPlantList()) {
            Plant plant = plantRepository.findById(plantId).orElseThrow(NoSuchElementException::new);

            plant.updatePlace(place);
            plantRepository.save(plant);
        }

        return place;
    }

    public void updateConditionDate(Plant plant){
        plantRepository.save(plant.updateConditionDate());
    }

    @Override
    public void deleteBy(Long id){
        plantRepository.deleteById(id);
    }
}
