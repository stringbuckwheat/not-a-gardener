package com.buckwheat.garden.service.impl;

import com.buckwheat.garden.dao.PlantDao;
import com.buckwheat.garden.data.dto.PlantRequestDto;
import com.buckwheat.garden.data.dto.PlantDto;
import com.buckwheat.garden.data.dto.WaterDto;
import com.buckwheat.garden.data.entity.Plant;
import com.buckwheat.garden.data.entity.Watering;
import com.buckwheat.garden.service.PlantService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class PlantServiceImpl implements PlantService {
    @Autowired
    private PlantDao plantDao;

    @Override
    public PlantDto getOnePlant(int plantNo) {
        Plant plant = plantDao.getPlantOne(plantNo);
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
        plantDao.savePlant(plantRequestDto.toEntity());
    }

    @Override
    public void modifyPlant(PlantRequestDto plantRequestDto) {
        Plant plant = plantDao.getPlantOne(plantRequestDto.getPlantNo())
                .update(plantRequestDto.getPlantName(),
                        plantRequestDto.getPlantSpecies(),
                        plantRequestDto.getAverageWateringPeriod());
        plantDao.savePlant(plant);
    }

    @Override
    public void deletePlantByPlantNo(int plantNo) {
        plantDao.deletePlantByPlantNo(plantNo);
    }
}
