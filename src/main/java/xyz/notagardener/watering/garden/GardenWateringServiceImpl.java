package xyz.notagardener.watering.garden;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.notagardener.common.error.code.ExceptionCode;
import xyz.notagardener.common.error.exception.UnauthorizedAccessException;
import xyz.notagardener.plant.Plant;
import xyz.notagardener.plant.garden.dto.GardenResponse;
import xyz.notagardener.plant.garden.dto.PlantResponse;
import xyz.notagardener.plant.garden.service.GardenResponseMapperImpl;
import xyz.notagardener.plant.garden.service.WateringCode;
import xyz.notagardener.plant.plant.repository.PlantRepository;
import xyz.notagardener.watering.garden.dto.GardenWateringResponse;
import xyz.notagardener.watering.watering.AfterWateringCode;
import xyz.notagardener.watering.watering.dto.AfterWatering;
import xyz.notagardener.watering.watering.dto.WateringMessage;
import xyz.notagardener.watering.watering.dto.WateringRequest;
import xyz.notagardener.watering.watering.service.WateringCommandService;

import java.time.Duration;
import java.time.LocalDate;
import java.util.NoSuchElementException;

@Service
@Slf4j
@RequiredArgsConstructor
public class GardenWateringServiceImpl implements GardenWateringService {
    private final WateringCommandService wateringCommandService;
    private final PlantRepository plantRepository;
    private final GardenResponseMapperImpl gardenResponseMapper;

    @Override
    @Transactional
    public GardenWateringResponse add(Long gardenerId, WateringRequest wateringRequest) {
        AfterWatering afterWatering = wateringCommandService.add(wateringRequest, gardenerId);
        GardenResponse gardenResponse = gardenResponseMapper.getGardenResponse(PlantResponse.from(afterWatering.getPlant()), gardenerId);

        return new GardenWateringResponse(gardenResponse, afterWatering.getWateringMessage());
    }

    private Plant getPlantByPlantIdAndGardenerId(Long plantId, Long gardenerId) {
        Plant plant = plantRepository.findByPlantId(plantId).orElseThrow(NoSuchElementException::new);

        if(!plant.getGardener().getGardenerId().equals(gardenerId)) {
            throw new UnauthorizedAccessException(ExceptionCode.NOT_YOUR_PLANT);
        }

        return plant;
    }

    @Override
    @Transactional
    public WateringMessage notDry(Long plantId, Long gardenerId) {
        Plant plant = getPlantByPlantIdAndGardenerId(plantId, gardenerId);

        // 한 번도 물 준 적 없는 경우
        if (plant.getWaterings().size() == 0) {
            plant.updateConditionDate();
            return new WateringMessage(AfterWateringCode.NO_RECORD.getCode(), 0);
        }

        // averageWateringPeriod 안 마른 날짜만큼 업데이트
        // 마지막으로 물 준 날짜와 오늘과의 날짜 사이를 계산함
        LocalDate lastDrinkingDay = plant.getWaterings().get(0).getWateringDate();
        System.out.println(lastDrinkingDay);
        int period = (int) Duration.between(lastDrinkingDay.atStartOfDay(), LocalDate.now().atStartOfDay()).toDays();

        // 오늘 한정 garden에 안 뜨게 해야함 => conditionDate 오늘 날짜 추가
        plant.updateConditionDate();

        if (period + 1 == plant.getRecentWateringPeriod()) {
            System.out.println("NO CHANGE");
            // 하루 전에 체크하라고 카드 띄웠는데 not Dry를 누른 경우 -> averageWateringPeriod 변경할 필요 없음
            return new WateringMessage(AfterWateringCode.NO_CHANGE.getCode(), plant.getRecentWateringPeriod());
        } else if (period >= plant.getRecentWateringPeriod()) {
            // 물 줄 때 됐는데 not dry -> 물주기 늘어남
            plant.updateRecentWateringPeriod(period + 1);
            return new WateringMessage(AfterWateringCode.SCHEDULE_LENGTHEN.getCode(), plant.getRecentWateringPeriod());
        }

        return null;
    }

    @Override
    @Transactional
    public String postpone(Long plantId, Long gardenerId) {
        Plant plant = getPlantByPlantIdAndGardenerId(plantId, gardenerId);
        // 미룰래요(그냥 귀찮아서 물주기 미룬 경우) == averageWateringPeriod 업데이트 안함!!
        // postponeDate를 업데이트함
        plant.updatePostponeDate();

        // waitingList에서는 오늘 하루만 없애줌
        return WateringCode.YOU_ARE_LAZY.getCode();
    }
}
