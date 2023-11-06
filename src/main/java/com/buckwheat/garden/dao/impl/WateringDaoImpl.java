package com.buckwheat.garden.dao.impl;

import com.buckwheat.garden.code.AfterWateringCode;
import com.buckwheat.garden.dao.WateringDao;
import com.buckwheat.garden.data.dto.watering.AfterWatering;
import com.buckwheat.garden.data.dto.watering.WateringMessage;
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

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
    public AfterWatering addWatering(WateringRequest wateringRequest) {
        Boolean exist = wateringRepository.existByWateringDateAndPlantId(wateringRequest.getWateringDate(), wateringRequest.getPlantId());

        if (exist) {
            throw new AlreadyWateredException();
        }

        // 맹물 줬는지 비료 타서 줬는지
        Chemical chemical = chemicalRepository.findById(wateringRequest.getChemicalId()).orElse(null);
        Plant plant = plantRepository.findByPlantId(wateringRequest.getPlantId())
                .orElseThrow(NoSuchElementException::new);
        plant.initConditionDateAndPostponeDate();

        plant.getWaterings().add(0, wateringRequest.toEntityWithPlantAndChemical(plant, chemical));

        // 관수 주기 업데이트
        WateringMessage wateringMessage = updateWateringPeriod(plant);

        // 저장
        return new AfterWatering(plant, wateringMessage);
    }

    @Override
    public WateringMessage updateWateringPeriod(Plant plant) {
        // 첫번째 물주기면
        if (plant.getWaterings().size() == 1) {
            return new WateringMessage(AfterWateringCode.FIRST_WATERING.getCode(), plant.getRecentWateringPeriod());
        } else if (plant.getWaterings().size() == 2) {
            return new WateringMessage(AfterWateringCode.SECOND_WATERING.getCode(), plant.getRecentWateringPeriod());
        }

        LocalDateTime latestWateringDate = plant.getWaterings().get(0).getWateringDate().atStartOfDay();
        log.debug("latestWateringDate: {}", latestWateringDate);
        LocalDateTime prevWateringDate = plant.getWaterings().get(1).getWateringDate().atStartOfDay();
        log.debug("prevWateringDate: {}", prevWateringDate);

        int period = (int) Duration.between(prevWateringDate, latestWateringDate).toDays();

        if (plant.getWaterings().size() == 3) {
            // 첫 물주기 측정 완료
            return new WateringMessage(AfterWateringCode.INIT_WATERING_PERIOD.getCode(), period);
        }

        // 물주기 짧아짐: -1
        // 물주기 똑같음: 0
        // 물주기 길어짐: 1
        int afterWateringCode = Integer.compare(period, plant.getRecentWateringPeriod());

        // 물주기 간격 변화 시 저장
        if (period != plant.getRecentWateringPeriod()) {
            plant.updateRecentWateringPeriod(period);
            plantRepository.save(plant);
        }

        return new WateringMessage(afterWateringCode, period);
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
        return wateringRepository.findLatestChemicalizedDayList(gardenerId, plantId, "Y");
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
    public AfterWatering modifyWatering(WateringRequest wateringRequest, Long gardenerId) {
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
    public WateringMessage deleteById(Long wateringId, Long plantId, Long gardenerId) {
        wateringRepository.deleteById(wateringId);
        Plant plant = plantRepository.findByPlantIdAndGardener_GardenerId(plantId, gardenerId).orElseThrow(NoSuchElementException::new);
        return updateWateringPeriod(plant);
    }

    @Override
    public void deleteByPlantId(Long plantId) {
        wateringRepository.deleteAllByPlant_PlantId(plantId);

        Plant plant = plantRepository.findById(plantId).orElseThrow(NoSuchElementException::new);
        // Recent Watering Period 초기화
        plant.updateRecentWateringPeriod(0);
    }
}
