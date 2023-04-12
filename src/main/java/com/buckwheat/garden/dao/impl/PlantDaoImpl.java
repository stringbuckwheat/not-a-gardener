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
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.NoSuchElementException;

@Repository
@RequiredArgsConstructor
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
    public Plant save(PlantDto.PlantRequest plantRequestDto, int memberNo) {
        Place place = placeRepository.findByPlaceNo(plantRequestDto.getPlaceNo()).orElseThrow(NoSuchElementException::new);
        Member member = memberRepository.findById(memberNo).orElseThrow(NoSuchElementException::new);

        return plantRepository.save(plantRequestDto.toEntityWith(member, place));
    }

    @Override
    public Plant update(PlantDto.PlantRequest plantRequest){
        Place place = placeRepository.findByPlaceNo(plantRequest.getPlaceNo()).orElseThrow(NoSuchElementException::new);
        Plant plant = plantRepository.findByPlantNo(plantRequest.getPlantNo()).orElseThrow(NoSuchElementException::new);

        return plantRepository.save(plant.update(plantRequest, place));
    }

    @Override
    public Place updatePlantPlace(PlaceDto.ModifyPlantPlaceDto modifyPlantPlaceDto){
        Place place = placeRepository.findByPlaceNo(modifyPlantPlaceDto.getPlaceNo()).orElseThrow(NoSuchElementException::new);

        for (int plantNo : modifyPlantPlaceDto.getPlantList()) {
            Plant plant = plantRepository.findById(plantNo).orElseThrow(NoSuchElementException::new);

            plant.updatePlace(place);
            plantRepository.save(plant);
        }

        return place;
    }

    @Override
    public void deletePlantByPlantNo(int plantNo){
        plantRepository.deleteById(plantNo);
    }
}
