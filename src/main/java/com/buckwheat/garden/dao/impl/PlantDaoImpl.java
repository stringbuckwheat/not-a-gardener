package com.buckwheat.garden.dao.impl;

import com.buckwheat.garden.dao.PlantDao;
import com.buckwheat.garden.data.entity.Plant;
import com.buckwheat.garden.repository.LoginRepository;
import com.buckwheat.garden.repository.PlantRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class PlantDaoImpl implements PlantDao {

    @Autowired
    private PlantRepository plantRepository;

    // 유저의 전체 식물리스트를 반환
    @Override
    public List<Plant> getPlantListByUsername(String id) {
        return plantRepository.findAllByUsername(id);
    }

    @Override
    public Plant savePlant(Plant plant) {
        return plantRepository.save(plant);
    }

    @Override
    public Plant getPlantOne(int plantNo) {
        return plantRepository.findById(plantNo).get();
    }

    @Override
    public void updateAverageWateringPeriod(int plantNo, int avgWateringPeriod) {
        Plant plant = plantRepository.findById(plantNo).get();
        plant.setAverageWateringPeriod(avgWateringPeriod);

        // plantNo 값이 있으므로 update가 실행된다.
        plantRepository.save(plant);
    }
}