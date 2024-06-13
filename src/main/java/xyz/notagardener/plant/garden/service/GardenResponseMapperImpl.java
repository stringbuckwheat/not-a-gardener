package xyz.notagardener.plant.garden.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import xyz.notagardener.plant.garden.dto.*;
import xyz.notagardener.repot.repot.service.RepotAlarmService;
import xyz.notagardener.watering.watering.dto.ChemicalUsage;
import xyz.notagardener.plant.garden.dto.WateringResponse;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class GardenResponseMapperImpl implements GardenResponseMapper {
    private final ChemicalInfoService chemicalInfoService;
    private final RepotAlarmService repotAlarmService;

    @Override
    public GardenResponse getGardenResponse(PlantResponse plantResponse, Long gardenerId) {
        GardenDetail gardenDetail = getGardenDetail(plantResponse, gardenerId);
        boolean isRepotNeeded = repotAlarmService.isRepotNeeded(plantResponse);

        return new GardenResponse(plantResponse, gardenDetail, isRepotNeeded);
    }

    private boolean isPostponeDay(LocalDate postponeDate) {
        return postponeDate != null && LocalDate.now().isEqual(postponeDate);
    }

    private GardenDetail getGardenDetail(PlantResponse plantResponse, Long gardenerId) {
        LocalDate lastDrinkingDay = plantResponse.getLatestWateringDate();
        int wateringPeriod = plantResponse.getRecentWateringPeriod();

        if (isPostponeDay(plantResponse.getPostponeDate())) {
            // 오늘 물주기를 미뤘는지
            return GardenDetail.lazy(lastDrinkingDay, plantResponse.getBirthday());
        }

        if (lastDrinkingDay == null) {
            // 물주기 정보 부족
            return GardenDetail.notEnoughRecord(plantResponse.getBirthday());
        }

        // 물 준지 며칠이나 지났는지
        int wateringDDay = getWateringDDay(wateringPeriod, lastDrinkingDay);
        String wateringCode = getWateringCode(wateringPeriod, wateringDDay); // 코드 계산

        // chemicalInfo: 맹물을 줄지 비료/약품 희석액을 줄지, 줘야한다면 어떤 비료를 줘야하는지
        ChemicalInfo chemicalInfo = null;

        if (WateringCode.THIRSTY.getCode().equals(wateringCode) || WateringCode.CHECK.getCode().equals(wateringCode)) {
            chemicalInfo = getChemicalInfo(plantResponse.getId(), gardenerId);
        }

        return GardenDetail.builder()
                .latestWateringDate(WateringResponse.from(lastDrinkingDay))
                .anniversary(GardenDetail.getAnniversary(plantResponse.getBirthday()))
                .wateringDDay(wateringDDay)
                .wateringCode(wateringCode)
                .chemicalInfo(chemicalInfo)
                .build();
    }

    private int getWateringDDay(int recentWateringPeriod, LocalDate lastDrinkingDay) {
        // 비료든 물이든 뭐라도 준지 며칠이나 지났는지 계산
        int period = (int) Duration.between(lastDrinkingDay.atStartOfDay(), LocalDate.now().atStartOfDay()).toDays();
        return recentWateringPeriod - period;
    }

    private String getWateringCode(int recentWateringPeriod, int wateringDDay) {
        if (recentWateringPeriod == wateringDDay) {
            return WateringCode.WATERED_TODAY.getCode(); // 오늘 물 줌
        } else if (recentWateringPeriod == 0) {
            return WateringCode.NOT_ENOUGH_RECORD.getCode(); // 물주기 정보 부족
        } else if (wateringDDay == 0) {
            return WateringCode.THIRSTY.getCode(); // 물주기
        } else if (wateringDDay == 1) {
            return WateringCode.CHECK.getCode(); // 물주기 하루 전, 체크하세요
        } else if (wateringDDay >= 2) {
            return WateringCode.LEAVE_HER_ALONE.getCode(); // 물주기까지 이틀 이상 남음, 놔두세요
        } else {
            return WateringCode.LATE_WATERING.getCode();
        }
    }

    public ChemicalInfo getChemicalInfo(Long plantId, Long gardenerId) {
        List<ChemicalUsage> latestChemicalUsages = chemicalInfoService.getChemicalUsagesByPlantId(plantId, gardenerId);

        for (ChemicalUsage latestFertilizingInfo : latestChemicalUsages) {
            LocalDate latestFertilizedDate = latestFertilizingInfo.getLatestWateringDate();
            if (latestFertilizedDate != null) {
                int period = (int) Duration.between(latestFertilizedDate.atStartOfDay(), LocalDate.now().atStartOfDay()).toDays();
                if (period >= (int) latestFertilizingInfo.getPeriod()) {
                    return new ChemicalInfo(latestFertilizingInfo.getChemicalId(), latestFertilizingInfo.getName());
                }
            }
        }

        return null;
    }
}
