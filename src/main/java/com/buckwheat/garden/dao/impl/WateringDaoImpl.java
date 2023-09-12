package com.buckwheat.garden.dao.impl;

import com.buckwheat.garden.dao.WateringDao;
import com.buckwheat.garden.data.dto.WateringDto;
import com.buckwheat.garden.data.entity.Chemical;
import com.buckwheat.garden.data.entity.Plant;
import com.buckwheat.garden.data.entity.Watering;
import com.buckwheat.garden.data.projection.ChemicalUsage;
import com.buckwheat.garden.error.exception.AlreadyWateredException;
import com.buckwheat.garden.repository.ChemicalRepository;
import com.buckwheat.garden.repository.PlantRepository;
import com.buckwheat.garden.repository.WateringRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

@Repository
@RequiredArgsConstructor
public class WateringDaoImpl implements WateringDao {
    private final WateringRepository wateringRepository;
    private final ChemicalRepository chemicalRepository;
    private final PlantRepository plantRepository;

    @Override
    @Transactional
    public Watering addWatering(WateringDto.Request wateringRequest) {
        Watering watering = wateringRepository.findByWateringDateAndPlant_PlantId(wateringRequest.getWateringDate(), wateringRequest.getPlantId());

        if(watering != null){
            throw new AlreadyWateredException();
        }

        Plant plant = plantRepository.findByPlantId(wateringRequest.getPlantId())
                .orElseThrow(NoSuchElementException::new);

        // conditionDate, postponeDate 초기화
        plant = plant.initConditionDateAndPostponeDate();
        plantRepository.save(plant);

        // 맹물 줬는지 비료 타서 줬는지
        Chemical chemical = chemicalRepository.findById(wateringRequest.getChemicalId()).orElse(null);

        // 저장
        return wateringRepository.save(wateringRequest.toEntityWithPlantAndChemical(plant, chemical));
    }

    @Override
    public List<Watering> getWateringListByPlantId(Long plantId) {
        return wateringRepository.findByPlant_PlantIdOrderByWateringDateDesc(plantId);
    }

    @Override
    public List<Watering> getAllWateringListByGardenerId(Long gardenerId, LocalDate startDate, LocalDate endDate) {
        return wateringRepository.findAllWateringListByGardenerId(gardenerId, startDate, endDate);
    }


    @Override
    public List<ChemicalUsage> getLatestChemicalUsages(Long gardenerId, Long plantId) {
        return wateringRepository.findLatestChemicalizedDayList(gardenerId, plantId);
    }

    @Override
    public Watering modifyWatering(WateringDto.Request wateringRequest) {
        // Mapping할 Entity 가져오기
        // chemical은 nullable이므로 orElse 사용
        Plant plant = plantRepository.findByPlantId(wateringRequest.getPlantId())
                .orElseThrow(NoSuchElementException::new);

        Chemical chemical = null;

        if (wateringRequest.getChemicalId() != null) {
            chemicalRepository.findById(wateringRequest.getChemicalId()).orElse(null);
        }

        // 기존 watering 엔티티
        Watering watering = wateringRepository.findById(wateringRequest.getId())
                .orElseThrow(NoSuchElementException::new);

        // 수정
        watering = wateringRepository.save(watering.update(wateringRequest.getWateringDate(), plant, chemical));
        return watering.update(wateringRequest.getWateringDate(), plant, chemical);
    }

    @Override
    public void deleteById(Long id) {
        wateringRepository.deleteById(id);
    }

    @Override
    public void deleteByPlantId(Long plantId) {
        wateringRepository.deleteAllByPlant_PlantId(plantId);
    }
}
