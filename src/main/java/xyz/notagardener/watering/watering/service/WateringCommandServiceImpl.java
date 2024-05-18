package xyz.notagardener.watering.watering.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.notagardener.chemical.Chemical;
import xyz.notagardener.chemical.repository.ChemicalRepository;
import xyz.notagardener.common.error.exception.AlreadyWateredException;
import xyz.notagardener.common.error.exception.UnauthorizedAccessException;
import xyz.notagardener.plant.Plant;
import xyz.notagardener.plant.plant.repository.PlantRepository;
import xyz.notagardener.watering.Watering;
import xyz.notagardener.watering.watering.AfterWateringCode;
import xyz.notagardener.watering.watering.dto.AfterWatering;
import xyz.notagardener.watering.watering.dto.WateringMessage;
import xyz.notagardener.watering.watering.dto.WateringRequest;
import xyz.notagardener.watering.watering.repository.WateringRepository;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Slf4j
public class WateringCommandServiceImpl implements WateringCommandService {
    private final WateringRepository wateringRepository;
    private final ChemicalRepository chemicalRepository;
    private final PlantRepository plantRepository;

    private void validateWatering(WateringRequest wateringRequest) {
        if (wateringRepository.existByWateringDateAndPlantId(wateringRequest.getWateringDate(), wateringRequest.getPlantId())) {
            throw new AlreadyWateredException();
        }
    }

    private Chemical getChemical(Long chemicalId, Long gardnerId) {
        if (chemicalId == null) return null;

        Chemical chemical = chemicalRepository.findById(chemicalId).orElseThrow(NoSuchElementException::new);

        if(chemical != null && !chemical.getGardener().getGardenerId().equals(gardnerId)) {
            throw new UnauthorizedAccessException("CHEMICAL", gardnerId);
        }

        return chemical;
    }

    private Plant getPlant(Long plantId, Long gardenerId) {
        Plant plant = plantRepository.findByPlantId(plantId).orElseThrow(NoSuchElementException::new);

        if(!plant.getGardener().getGardenerId().equals(gardenerId)) {
            throw new UnauthorizedAccessException("PLANT", gardenerId);
        }

        return plant;
    }

    @Override
    @Transactional
    public AfterWatering add(WateringRequest wateringRequest, Long gardenerId) {
        validateWatering(wateringRequest);

        // 맹물 줬는지 비료 타서 줬는지
        Chemical chemical = getChemical(wateringRequest.getChemicalId(), gardenerId);
        Plant plant = getPlant(wateringRequest.getPlantId(), gardenerId);

        plant.initConditionDateAndPostponeDate(); // 물 안 마른 날, 물주기 미룬 날 초기화
        plant.getWaterings().add(wateringRequest.toEntityWithPlantAndChemical(plant, chemical));
        plantRepository.save(plant);

        // 관수 주기 계산 및 업데이트
        WateringMessage afterWatering = updateWateringPeriod(plant);
        return new AfterWatering(plant, afterWatering);
    }

    @Override
    @Transactional
    public WateringMessage updateWateringPeriod(Plant plant) {
        List<Watering> waterings = wateringRepository.findLatestFourWateringDate(plant.getPlantId());
        WateringMessage afterWatering = calculateWateringPeriod(waterings);

        log.debug("{}의 이전 관수주기: {}, 현재 관수주기: {}", plant.getName(), plant.getRecentWateringPeriod(), afterWatering.getRecentWateringPeriod());

        updatePlantWateringPeriod(plant, afterWatering);

        return afterWatering;
    }

    private void updatePlantWateringPeriod(Plant plant, WateringMessage afterWatering) {
        int afterWateringCode = afterWatering.getAfterWateringCode();
        int recentWateringPeriod = afterWatering.getRecentWateringPeriod();

        // 첫 recent watering period 기록
        if (afterWateringCode == AfterWateringCode.INIT_WATERING_PERIOD.getCode()) {
            // 초기 관수 주기 저장
            plant.updateRecentWateringPeriod(recentWateringPeriod);
            plant.initEarlyWateringPeriod(recentWateringPeriod);
        } else if (recentWateringPeriod != plant.getRecentWateringPeriod()) {
            plant.updateRecentWateringPeriod(recentWateringPeriod);
        }
    }

    private WateringMessage calculateWateringPeriod(List<Watering> waterings){
        if (waterings.isEmpty()) return new WateringMessage(AfterWateringCode.NO_RECORD.getCode(), 0);

        int prevPeriod = waterings.get(0).getPlant().getRecentWateringPeriod();

        if (waterings.size() == 1) return new WateringMessage(AfterWateringCode.FIRST_WATERING.getCode(), prevPeriod);
        else if (waterings.size() == 2) return new WateringMessage(AfterWateringCode.SECOND_WATERING.getCode(), prevPeriod);

        LocalDateTime latestWateringDate = waterings.get(0).getWateringDate().atStartOfDay();
        LocalDateTime prevWateringDate = waterings.get(1).getWateringDate().atStartOfDay();
        int period = (int) Duration.between(prevWateringDate, latestWateringDate).toDays();

        if (waterings.size() == 3) return new WateringMessage(AfterWateringCode.INIT_WATERING_PERIOD.getCode(), period);

        // 물주기 짧아짐: -1, 물주기 똑같음: 0, 물주기 길어짐: 1
        int afterWateringCode = Integer.compare(period, prevPeriod);

        return new WateringMessage(afterWateringCode, period);
    }

    @Override
    @Transactional
    public AfterWatering update(WateringRequest wateringRequest, Long gardenerId) {
        Plant plant = getPlant(wateringRequest.getPlantId(), gardenerId);
        Chemical chemical = getChemical(wateringRequest.getChemicalId(), gardenerId);

        // 기존 watering 엔티티
        Watering watering = wateringRepository.findByWateringIdAndPlant_Gardener_GardenerId(wateringRequest.getId(), gardenerId)
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
        Watering watering = wateringRepository.findByWateringIdAndPlant_Gardener_GardenerId(wateringId, gardenerId)
                .orElseThrow(NoSuchElementException::new);
        wateringRepository.delete(watering);

        Plant plant = getPlant(plantId, gardenerId);
        return updateWateringPeriod(plant);
    }

    @Override
    @Transactional
    public void deleteByPlantId(Long plantId, Long gardenerId) {
        Plant plant = getPlant(plantId, gardenerId);
        wateringRepository.deleteAllByPlant_PlantId(plantId);

        // 물주기 Period 초기화
        plant.updateRecentWateringPeriod(0);
        plant.initEarlyWateringPeriod(0);
    }
}
