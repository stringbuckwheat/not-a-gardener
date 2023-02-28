package com.buckwheat.garden.service.impl;

import com.buckwheat.garden.data.dto.PlantDto;
import com.buckwheat.garden.data.dto.PlantRequestDto;
import com.buckwheat.garden.data.dto.GardenDto;
import com.buckwheat.garden.data.dto.WaterDto;
import com.buckwheat.garden.data.entity.Place;
import com.buckwheat.garden.data.entity.Plant;
import com.buckwheat.garden.data.entity.Watering;
import com.buckwheat.garden.repository.PlaceRepository;
import com.buckwheat.garden.repository.PlantRepository;
import com.buckwheat.garden.service.PlantService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
    public GardenDto getOnePlant(int plantNo) {
        Plant plant = plantRepository.findById(plantNo).orElseThrow(NoSuchElementException::new);
        log.debug("plant: " + plant);

        // DTO로 변환
        GardenDto gardenDto = new GardenDto(plant);

        List<WaterDto> waterDtoList = new ArrayList<>();

        for(Watering w : plant.getWateringList()){
            waterDtoList.add(new WaterDto(w));
        }

        gardenDto.setWaterDtoList(waterDtoList);

        log.debug("plantDto: " + gardenDto);
        // return plantDto;

        return null;
    }

    @Override
    public List<PlantDto> getPlantList(int memberNo) {
        log.debug("getPlantList");
        List<PlantDto> plantList = new ArrayList<>();

        for(Plant p : plantRepository.findByMember_MemberNo(memberNo)){
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
    public void addPlant(PlantRequestDto plantRequestDto) {
        Place place = placeRepository.findById(plantRequestDto.getPlaceNo()).orElseThrow(NoSuchElementException::new);
        // Member member = memberRepository.findById(plantRequestDto.getMemberNo()).orElseThrow(NoSuchElementException::new);

        Plant plant = Plant.builder()
                .plantName(plantRequestDto.getPlantName())
                .plantSpecies(plantRequestDto.getPlantSpecies())
                .medium(plantRequestDto.getMedium())
                .createDate(LocalDateTime.now())
                .earlyWateringPeriod(plantRequestDto.getAverageWateringPeriod()) // 초기값은 유저가 입력한 평균 물주기
                .averageWateringPeriod(plantRequestDto.getAverageWateringPeriod())
                .place(place)
                .member(plantRequestDto.getMember())
                .build();

        plantRepository.save(plant);
    }

    @Override
    public void modifyPlant(PlantRequestDto plantRequestDto) {
        Plant plant = plantRepository.findById(plantRequestDto.getPlantNo())
                .orElseThrow(NoSuchElementException::new)
                .update(plantRequestDto.getPlantName(), plantRequestDto.getPlantSpecies(), plantRequestDto.getMedium());

        plantRepository.save(plant);
    }

    @Override
    public void deletePlantByPlantNo(int plantNo) {
        plantRepository.deleteById(plantNo);
    }
}
