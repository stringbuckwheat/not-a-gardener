package com.buckwheat.garden.repository.command.impl;

import com.buckwheat.garden.PlantUtils;
import com.buckwheat.garden.code.AfterWateringCode;
import com.buckwheat.garden.dao.ChemicalDao;
import com.buckwheat.garden.dao.PlantDao;
import com.buckwheat.garden.dao.WateringDao;
import com.buckwheat.garden.data.dto.watering.AfterWatering;
import com.buckwheat.garden.data.dto.watering.WateringMessage;
import com.buckwheat.garden.data.dto.watering.WateringRequest;
import com.buckwheat.garden.data.entity.Chemical;
import com.buckwheat.garden.data.entity.Plant;
import com.buckwheat.garden.data.entity.Watering;
import com.buckwheat.garden.error.exception.AlreadyWateredException;
import com.buckwheat.garden.repository.command.WateringCommandRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Repository
@RequiredArgsConstructor
@Slf4j
public class WateringCommandRepositoryImpl implements WateringCommandRepository {
    private final WateringDao wateringDao;
    private final ChemicalDao chemicalDao;
    private final PlantDao plantDao;

    @Override
    @Transactional
    public AfterWatering add(WateringRequest wateringRequest) {
        Boolean exist = wateringDao.existByWateringDateAndPlantId(wateringRequest.getWateringDate(), wateringRequest.getPlantId());

        if (exist) {
            throw new AlreadyWateredException();
        }

        // 맹물 줬는지 비료 타서 줬는지
        Chemical chemical = chemicalDao.findById(wateringRequest.getChemicalId()).orElse(null);
        Plant plant = plantDao.findByPlantId(wateringRequest.getPlantId())
                .orElseThrow(NoSuchElementException::new);
        plant.initConditionDateAndPostponeDate();

        plant.getWaterings().add(wateringRequest.toEntityWithPlantAndChemical(plant, chemical));
        plantDao.save(plant);

        // 관수 주기 계산 및 업데이트
        WateringMessage afterWatering = updateWateringPeriod(plant);

        // 저장
        return new AfterWatering(plant, afterWatering);
    }

    @Override
    public WateringMessage updateWateringPeriod(Plant plant) {
        List<Watering> waterings = wateringDao.findLatestFourWateringDate(plant.getPlantId());
        WateringMessage afterWatering = PlantUtils.calculateWateringPeriod(waterings, plant.getRecentWateringPeriod());
        log.debug("{}의 이전 관수주기: {}, 현재 관수주기: {}", plant.getName(), plant.getRecentWateringPeriod(), afterWatering.getRecentWateringPeriod());

        // 첫 recent watering period 기록
        if(afterWatering.getAfterWateringCode() == AfterWateringCode.INIT_WATERING_PERIOD.getCode()){
            // 초기 관수 주기 저장
            plant.updateRecentWateringPeriod(afterWatering.getRecentWateringPeriod());
            plant.initEarlyWateringPeriod(afterWatering.getRecentWateringPeriod());
        } else if(afterWatering.getRecentWateringPeriod() != plant.getRecentWateringPeriod()){
            plant.updateRecentWateringPeriod(afterWatering.getRecentWateringPeriod());
        }

        return afterWatering;
    }

    @Override
    @Transactional
    public AfterWatering update(WateringRequest wateringRequest, Long gardenerId) {
        // Mapping할 Entity 가져오기
        // chemical은 nullable이므로 orElse 사용
        Plant plant = plantDao.findByPlantIdAndGardener_GardenerId(wateringRequest.getPlantId(), gardenerId)
                .orElseThrow(NoSuchElementException::new);

        Chemical chemical = null;

        if (wateringRequest.getChemicalId() != null) {
            chemical = chemicalDao.findById(wateringRequest.getChemicalId()).orElse(null);
        }

        // 기존 watering 엔티티
        Watering watering = wateringDao.findById(wateringRequest.getId())
                .orElseThrow(NoSuchElementException::new);
        // watering 수정
        wateringDao.save(watering.update(wateringRequest.getWateringDate(), plant, chemical));

        // recent watering period 수정
        WateringMessage wateringMessage = updateWateringPeriod(plant);

        return new AfterWatering(plant, wateringMessage);
    }

    @Override
    @Transactional
    public WateringMessage deleteById(Long wateringId, Long plantId, Long gardenerId) {
        wateringDao.deleteById(wateringId);
        Plant plant = plantDao.findByPlantIdAndGardener_GardenerId(plantId, gardenerId).orElseThrow(NoSuchElementException::new);
        return updateWateringPeriod(plant);
    }

    @Override
    @Transactional
    public void deleteByPlantId(Long plantId) {
        wateringDao.deleteAllByPlant_PlantId(plantId);

        Plant plant = plantDao.findById(plantId).orElseThrow(NoSuchElementException::new);
        // 물주기 Period 초기화
        plant.updateRecentWateringPeriod(0);
        plant.initEarlyWateringPeriod(0);
    }
}
