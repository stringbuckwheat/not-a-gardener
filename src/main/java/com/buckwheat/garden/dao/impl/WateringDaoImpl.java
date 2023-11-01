package com.buckwheat.garden.dao.impl;

import com.buckwheat.garden.dao.WateringDao;
import com.buckwheat.garden.data.dto.watering.WateringRequest;
import com.buckwheat.garden.data.entity.Chemical;
import com.buckwheat.garden.data.entity.Plant;
import com.buckwheat.garden.data.entity.Watering;
import com.buckwheat.garden.data.projection.ChemicalUsage;
import com.buckwheat.garden.error.exception.AlreadyWateredException;
import com.buckwheat.garden.repository.ChemicalRepository;
import com.buckwheat.garden.repository.PlantRepository;
import com.buckwheat.garden.repository.WateringRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

@Repository
@RequiredArgsConstructor
@Slf4j
public class WateringDaoImpl implements WateringDao {
    private final WateringRepository wateringRepository;
    private final ChemicalRepository chemicalRepository;
    private final PlantRepository plantRepository;

    @Override
    @Transactional
    public Watering addWatering(WateringRequest wateringRequest) {
        // TODO 단순 존재 유무만 쿼리
        // Querydsl 의 exist 는 내부적으로 count 쿼리를 사용하고 있기에, limit(1) 을 직접 구현해서 사용하는 것이 좋습니다.
        Watering w = wateringRepository.findByWateringDateAndPlant_PlantId(wateringRequest.getWateringDate(), wateringRequest.getPlantId());

        if (w != null) {
            throw new AlreadyWateredException();
        }

        // 맹물 줬는지 비료 타서 줬는지
        Chemical chemical = chemicalRepository.findById(wateringRequest.getChemicalId()).orElse(null);
        Plant plant = plantRepository.findByPlantId(wateringRequest.getPlantId())
                .orElseThrow(NoSuchElementException::new);
        plant.initConditionDateAndPostponeDate();

        Watering watering = wateringRepository.save(wateringRequest.toEntityWithPlantAndChemical(plant, chemical));
        plant.getWaterings().add(watering);

        // 저장
        return watering;
    }

    @Override
    public List<Watering> getWateringListByPlantId(Long plantId, Pageable pageable) {
        return wateringRepository.findWateringsByPlantIdWithPage(plantId, pageable);
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
    public List<Watering> getWateringsByChemicalIdWithPage(Long chemicalId, Pageable pageable) {
        return wateringRepository.findWateringsByChemicalIdWithPage(chemicalId, pageable);
    }

    @Override
    public int getCountByChemical_ChemicalId(Long chemicalId) {
        return wateringRepository.countByChemical_ChemicalId(chemicalId);
    }

    @Override
    @Transactional
    public Watering modifyWatering(WateringRequest wateringRequest) {
        // Mapping할 Entity 가져오기
        // chemical은 nullable이므로 orElse 사용
        Plant plant = plantRepository.findByPlantId(wateringRequest.getPlantId())
                .orElseThrow(NoSuchElementException::new);

        Chemical chemical = null;

        if (wateringRequest.getChemicalId() != null) {
            chemical = chemicalRepository.findById(wateringRequest.getChemicalId()).orElse(null);
        }

        // 기존 watering 엔티티
        Watering watering = wateringRepository.findById(wateringRequest.getId())
                .orElseThrow(NoSuchElementException::new);

        // 수정
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
