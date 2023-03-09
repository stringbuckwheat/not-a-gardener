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

import java.time.LocalDate;
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
    public List<PlantDto> getPlantList(int memberNo) {
        log.debug("getPlantList");
        List<PlantDto> plantList = new ArrayList<>();

        // @EntityGraph 메소드
        for(Plant p : plantRepository.findByMember_MemberNoOrderByCreateDateDesc(memberNo)){
            plantList.add(PlantDto.builder()
                            .plantNo(p.getPlantNo())
                            .placeNo(p.getPlace().getPlaceNo())
                            .placeName(p.getPlace().getPlaceName())
                            .plantSpecies(p.getPlantSpecies())
                            .averageWateringPeriod(p.getAverageWateringPeriod())
                            .medium(p.getMedium())
                            .plantName(p.getPlantName())
                            .createDate(LocalDate.from(p.getCreateDate()))
                    .build());
        }
        return plantList;
    }

    @Override
    public void addPlant(PlantDto.PlantRequestDto plantRequestDto, Member member) {
        Place place = placeRepository.findById(plantRequestDto.getPlaceNo()).orElseThrow(NoSuchElementException::new);

        plantRepository.save(plantRequestDto.toEntityWithMemberAndPlace(member, place));
    }

    @Override
    public void modifyPlant(PlantRequestDto plantRequestDto) {
        Plant plant = plantRepository.findById(plantRequestDto.getPlantNo())
                .orElseThrow(NoSuchElementException::new)
                .update(plantRequestDto.getPlantName(), plantRequestDto.getPlantSpecies(), plantRequestDto.getMedium());

        plantRepository.save(plant);
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

    /**
     * 장소 디테일 페이지에서 해당 장소에 속한 식물 리스트를 반환
     * @param placeNo 장소 번호(PK)
     * @return 해당 장소에 속한 식물(DTO) 리스트
     */
    @Override
    public List<PlantDto> getPlantlistInPlace(int placeNo) {
        List<PlantDto> plantList = new ArrayList<>();

        for(Plant pl : plantRepository.findByPlace_PlaceNo(placeNo)){
            plantList.add(
                    PlantDto.builder()
                        .plantNo(pl.getPlantNo())
                        .plantName(pl.getPlantName())
                        .plantSpecies(pl.getPlantSpecies())
                        .averageWateringPeriod(pl.getAverageWateringPeriod())
                        .medium(pl.getMedium())
                        .createDate(LocalDate.from(pl.getCreateDate()))
                        .build()

            );
        }
        return plantList;

    }
}
