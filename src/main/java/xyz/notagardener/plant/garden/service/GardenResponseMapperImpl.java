package xyz.notagardener.plant.garden.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import xyz.notagardener.plant.garden.dto.ChemicalInfo;
import xyz.notagardener.plant.garden.dto.GardenDetail;
import xyz.notagardener.plant.garden.dto.GardenResponse;
import xyz.notagardener.plant.garden.dto.RawGarden;
import xyz.notagardener.plant.plant.dto.PlantResponse;
import xyz.notagardener.watering.code.WateringCode;
import xyz.notagardener.watering.dto.ChemicalUsage;
import xyz.notagardener.watering.dto.WateringResponse;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class GardenResponseMapperImpl implements GardenResponseMapper {
    private final ChemicalInfoService chemicalInfoService;

    @Override
    public GardenResponse getGardenResponse(RawGarden rawGarden, Long gardenerId) {
        PlantResponse plantResponse = PlantResponse.from(rawGarden); // 식물 기본 정보, 가공 없이 리턴
        GardenDetail gardenDetail = getGardenDetail(rawGarden, gardenerId);
        return new GardenResponse(plantResponse, gardenDetail);
    }

    private boolean isPostponeDay(LocalDate postponeDate) {
        return postponeDate != null && LocalDate.now().isEqual(postponeDate);
    }

    private GardenDetail getGardenDetail(RawGarden rawGarden, Long gardenerId) {
        LocalDate lastDrinkingDay = rawGarden.getLatestWateringDate();
        int wateringPeriod = rawGarden.getRecentWateringPeriod();

        if (isPostponeDay(rawGarden.getPostponeDate())) {
            // 오늘 물주기를 미뤘는지
            return GardenDetail.lazy(lastDrinkingDay, rawGarden.getBirthday());
        }

        if (lastDrinkingDay == null) {
            // 물주기 정보 부족
            return GardenDetail.noRecord(rawGarden.getBirthday());
        }

        // 물 준지 며칠이나 지났는지
        int wateringDDay = getWateringDDay(wateringPeriod, lastDrinkingDay);
        int wateringCode = getWateringCode(wateringPeriod, wateringDDay); // 코드 계산

        // chemicalInfo: 맹물을 줄지 비료/약품 희석액을 줄지, 줘야한다면 어떤 비료를 줘야하는지
        ChemicalInfo chemicalInfo = null;

        if (wateringCode == WateringCode.THIRSTY.getCode() || wateringCode == WateringCode.CHECK.getCode()) {
            chemicalInfo = getChemicalInfo(rawGarden.getPlantId(), gardenerId);
        }

        return GardenDetail.builder()
                .latestWateringDate(WateringResponse.from(lastDrinkingDay))
                .anniversary(GardenDetail.getAnniversary(rawGarden.getBirthday()))
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

    private int getWateringCode(int recentWateringPeriod, int wateringDDay) {
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
            return wateringDDay; // 음수가 나왔으면 물주기 놓침, 며칠 늦었는지 알려줌
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
