package com.buckwheat.garden.domain.watering.service;

import com.buckwheat.garden.domain.chemical.repository.ChemicalRepository;
import com.buckwheat.garden.domain.watering.Watering;
import com.buckwheat.garden.domain.watering.repository.WateringRepository;
import com.buckwheat.garden.global.code.AfterWateringCode;
import com.buckwheat.garden.domain.chemical.Chemical;
import com.buckwheat.garden.domain.plant.Plant;
import com.buckwheat.garden.domain.plant.repository.PlantRepository;
import com.buckwheat.garden.domain.plant.PlantUtils;
import com.buckwheat.garden.domain.watering.dto.AfterWatering;
import com.buckwheat.garden.domain.watering.dto.WateringMessage;
import com.buckwheat.garden.domain.watering.dto.WateringRequest;
import com.buckwheat.garden.global.error.exception.AlreadyWateredException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Slf4j
public class WateringCommandServiceImpl implements WateringCommandService {
    private final WateringRepository wateringRepository;
    private final ChemicalRepository chemicalRepository;
    private final PlantRepository plantRepository;

    @Override
    @Transactional
    public AfterWatering add(WateringRequest wateringRequest) {
        Boolean exist = wateringRepository.existByWateringDateAndPlantId(wateringRequest.getWateringDate(), wateringRequest.getPlantId());

        if (exist) {
            throw new AlreadyWateredException();
        }

        // 맹물 줬는지 비료 타서 줬는지
        Chemical chemical = chemicalRepository.findById(wateringRequest.getChemicalId()).orElse(null);
        Plant plant = plantRepository.findByPlantId(wateringRequest.getPlantId())
                .orElseThrow(NoSuchElementException::new);
        plant.initConditionDateAndPostponeDate();

        plant.getWaterings().add(wateringRequest.toEntityWithPlantAndChemical(plant, chemical));
        plantRepository.save(plant);

        // 관수 주기 계산 및 업데이트
        WateringMessage afterWatering = updateWateringPeriod(plant);

        // 저장
        return new AfterWatering(plant, afterWatering);
    }

    @Override
    @Transactional
    public WateringMessage updateWateringPeriod(Plant plant) {
        List<Watering> waterings = wateringRepository.findLatestFourWateringDate(plant.getPlantId());
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
        Plant plant = plantRepository.findByPlantIdAndGardener_GardenerId(wateringRequest.getPlantId(), gardenerId)
                .orElseThrow(NoSuchElementException::new);

        Chemical chemical = null;

        if (wateringRequest.getChemicalId() != null) {
            chemical = chemicalRepository.findById(wateringRequest.getChemicalId()).orElse(null);
        }

        // 기존 watering 엔티티
        Watering watering = wateringRepository.findById(wateringRequest.getId())
                .orElseThrow(NoSuchElementException::new);
        // watering 수정
        wateringRepository.save(watering.update(wateringRequest.getWateringDate(), plant, chemical));

        // recent watering period 수정
        WateringMessage wateringMessage = updateWateringPeriod(plant);

        return new AfterWatering(plant, wateringMessage);
    }

    @Override
    @Transactional
    public WateringMessage deleteById(Long wateringId, Long plantId, Long gardenerId) {
        wateringRepository.deleteById(wateringId);
        Plant plant = plantRepository.findByPlantIdAndGardener_GardenerId(plantId, gardenerId).orElseThrow(NoSuchElementException::new);
        return updateWateringPeriod(plant);
    }

    @Override
    @Transactional
    public void deleteByPlantId(Long plantId) {
        wateringRepository.deleteAllByPlant_PlantId(plantId);

        Plant plant = plantRepository.findById(plantId).orElseThrow(NoSuchElementException::new);
        // 물주기 Period 초기화
        plant.updateRecentWateringPeriod(0);
        plant.initEarlyWateringPeriod(0);
    }
}
