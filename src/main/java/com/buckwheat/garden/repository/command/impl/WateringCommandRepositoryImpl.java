package com.buckwheat.garden.repository.command.impl;

import com.buckwheat.garden.code.AfterWateringCode;
import com.buckwheat.garden.data.dto.watering.AfterWatering;
import com.buckwheat.garden.data.dto.watering.WateringMessage;
import com.buckwheat.garden.data.dto.watering.WateringRequest;
import com.buckwheat.garden.data.entity.Chemical;
import com.buckwheat.garden.data.entity.Plant;
import com.buckwheat.garden.data.entity.Watering;
import com.buckwheat.garden.error.exception.AlreadyWateredException;
import com.buckwheat.garden.dao.ChemicalDao;
import com.buckwheat.garden.dao.PlantDao;
import com.buckwheat.garden.dao.WateringDao;
import com.buckwheat.garden.repository.command.WateringCommandRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
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

        // 관수 주기 업데이트
        WateringMessage wateringMessage = updateWateringPeriod(plant.getPlantId());

        // 저장
        return new AfterWatering(plant, wateringMessage);
    }

    @Override
    public WateringMessage updateWateringPeriod(Long plantId) {
        Plant plant = plantDao.findByPlantId(plantId).orElseThrow(NoSuchElementException::new);
        List<Watering> waterings = wateringDao.findLatestFourWateringDate(plantId);

        // 첫번째 물주기면
        if (waterings.size() == 0) {
            return null;
        } else if (waterings.size() == 1) {
            return new WateringMessage(AfterWateringCode.FIRST_WATERING.getCode(), plant.getRecentWateringPeriod());
        } else if (waterings.size() == 2) {
            return new WateringMessage(AfterWateringCode.SECOND_WATERING.getCode(), plant.getRecentWateringPeriod());
        }

        LocalDateTime latestWateringDate = waterings.get(0).getWateringDate().atStartOfDay();
        LocalDateTime prevWateringDate = waterings.get(1).getWateringDate().atStartOfDay();

        int period = (int) Duration.between(prevWateringDate, latestWateringDate).toDays();

        if (waterings.size() == 3) {
            // 첫 물주기 측정 완료
            plant.updateRecentWateringPeriod(period);
            plantDao.save(plant);
            return new WateringMessage(AfterWateringCode.INIT_WATERING_PERIOD.getCode(), period);
        }

        // 물주기 짧아짐: -1
        // 물주기 똑같음: 0
        // 물주기 길어짐: 1
        int afterWateringCode = Integer.compare(period, plant.getRecentWateringPeriod());

        // 물주기 간격 변화 시 저장
        if (period != plant.getRecentWateringPeriod()) {
            plant.updateRecentWateringPeriod(period);
            plantDao.save(plant);
        }

        return new WateringMessage(afterWateringCode, period);
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
        WateringMessage wateringMessage = updateWateringPeriod(plant.getPlantId());

        return new AfterWatering(plant, wateringMessage);
    }

    @Override
    @Transactional
    public WateringMessage deleteById(Long wateringId, Long plantId, Long gardenerId) {
        wateringDao.deleteById(wateringId);
        Plant plant = plantDao.findByPlantIdAndGardener_GardenerId(plantId, gardenerId).orElseThrow(NoSuchElementException::new);
        return updateWateringPeriod(plant.getPlantId());
    }

    @Override
    @Transactional
    public void deleteByPlantId(Long plantId) {
        wateringDao.deleteAllByPlant_PlantId(plantId);

        Plant plant = plantDao.findById(plantId).orElseThrow(NoSuchElementException::new);
        // Recent Watering Period 초기화
        plant.updateRecentWateringPeriod(0);
    }
}
