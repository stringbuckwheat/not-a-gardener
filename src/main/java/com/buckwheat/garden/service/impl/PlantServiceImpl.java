package com.buckwheat.garden.service.impl;

import com.buckwheat.garden.data.dto.PlantRequestDto;
import com.buckwheat.garden.data.dto.PlantDto;
import com.buckwheat.garden.data.dto.WaterDto;
import com.buckwheat.garden.data.entity.Plant;
import com.buckwheat.garden.data.entity.Watering;
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

    @Override
    public PlantDto getOnePlant(int plantNo) {
        Plant plant = plantRepository.findById(plantNo).orElseThrow(NoSuchElementException::new);
        log.debug("plant: " + plant);

        // DTO로 변환
        PlantDto plantDto = new PlantDto(plant);

        List<WaterDto> waterDtoList = new ArrayList<>();

        for(Watering w : plant.getWateringList()){
            waterDtoList.add(new WaterDto(w));
        }

        plantDto.setWaterDtoList(waterDtoList);

        log.debug("plantDto: " + plantDto);
        return plantDto;
    }

    @Override
    public void addPlant(PlantRequestDto plantRequestDto) {
        plantRepository.save(plantRequestDto.toEntity());
    }

    @Override
    public void modifyPlant(PlantRequestDto plantRequestDto) {
        Plant plant = plantRepository.findById(plantRequestDto.getPlantNo())
                .orElseThrow(NoSuchElementException::new)
                .update(plantRequestDto.getPlantName(),
                        plantRequestDto.getPlantSpecies(),
                        plantRequestDto.getAverageWateringPeriod());
        plantRepository.save(plant);
    }

    @Override
    public void deletePlantByPlantNo(int plantNo) {
        plantRepository.deleteById(plantNo);
    }
}
