package xyz.notagardener.watering.plant.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.notagardener.plant.Plant;
import xyz.notagardener.plant.garden.dto.PlantResponse;
import xyz.notagardener.watering.Watering;
import xyz.notagardener.watering.plant.dto.PlantWateringResponse;
import xyz.notagardener.watering.plant.dto.WateringForOnePlant;
import xyz.notagardener.watering.watering.dto.AfterWatering;
import xyz.notagardener.watering.watering.dto.WateringMessage;
import xyz.notagardener.watering.watering.dto.WateringRequest;
import xyz.notagardener.watering.watering.repository.WateringRepository;
import xyz.notagardener.watering.watering.service.WateringCommandService;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class PlantWateringServiceImpl implements PlantWateringService {
    private final WateringCommandService wateringCommandService;
    private final WateringRepository wateringQueryRepository;

    @Override
    @Transactional
    public PlantWateringResponse add(WateringRequest wateringRequest, Pageable pageable, Long gardenerId) {
        AfterWatering afterWatering = wateringCommandService.add(wateringRequest, gardenerId);

        List<WateringForOnePlant> waterings = getAll(wateringRequest.getPlantId(), pageable);

        return PlantWateringResponse.from(new PlantResponse(afterWatering.getPlant()), afterWatering.getWateringMessage(), waterings);
    }

    @Override
    @Transactional(readOnly = true)
    public List<WateringForOnePlant> getAll(Long plantId, Pageable pageable) {
        List<Watering> waterings = wateringQueryRepository.findWateringsByPlantIdWithPage(plantId, pageable); // orderByWateringDateDesc

        // 며칠만에 물 줬는지
        if (waterings.size() >= 2) {
            return withWateringPeriodList(waterings);
        }

        return waterings.stream().map(WateringForOnePlant::from).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    private List<WateringForOnePlant> withWateringPeriodList(List<Watering> waterings) {
        List<WateringForOnePlant> result = new ArrayList<>();

        for (int i = 0; i < waterings.size(); i++) {
            Watering currentWatering = waterings.get(i);

            // 마지막 요소이자 가장 옛날 물주기
            // => 전 요소가 없으므로 며칠만에 물 줬는지 계산 X
            if (i == waterings.size() - 1) {
                result.add(WateringForOnePlant.from(waterings.get(i)));
            } else {
                // 며칠만에 물줬는지 계산
                Watering nextWatering = waterings.get(i + 1);
                int wateringPeriod = calculateWateringPeriod(currentWatering, nextWatering);

                result.add(WateringForOnePlant.withWateringPeriodFrom(currentWatering, wateringPeriod));
            }
        }

        return result;
    }

    private int calculateWateringPeriod(Watering currentWatering, Watering nextWatering) {
        LocalDateTime currentWateringDate = currentWatering.getWateringDate().atStartOfDay();
        LocalDateTime nextWateringDate = nextWatering.getWateringDate().atStartOfDay();
        return (int) Duration.between(nextWateringDate, currentWateringDate).toDays();
    }

    @Override
    @Transactional
    public PlantWateringResponse update(WateringRequest wateringRequest, Pageable pageable, Long gardenerId) {
        AfterWatering afterWatering = wateringCommandService.update(wateringRequest, gardenerId);

        Plant plant = afterWatering.getPlant();
        WateringMessage wateringMsg = afterWatering.getWateringMessage();
        List<WateringForOnePlant> waterings = getAll(plant.getPlantId(), pageable);

        return PlantWateringResponse.from(new PlantResponse(plant), wateringMsg, waterings);
    }

    @Override
    public void delete(Long wateringId, Long plantId, Long gardenerId) {
        wateringCommandService.deleteById(wateringId, plantId, gardenerId);
    }

    @Override
    public void deleteAll(Long plantId, Long gardenerId) {
        wateringCommandService.deleteByPlantId(plantId, gardenerId);
    }
}
