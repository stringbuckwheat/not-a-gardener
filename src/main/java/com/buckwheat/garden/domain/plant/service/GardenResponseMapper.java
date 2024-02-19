package com.buckwheat.garden.domain.plant.service;

import com.buckwheat.garden.domain.plant.dto.garden.ChemicalInfo;
import com.buckwheat.garden.domain.plant.dto.garden.GardenDetail;
import com.buckwheat.garden.domain.plant.dto.garden.GardenResponse;
import com.buckwheat.garden.domain.plant.dto.plant.PlantResponse;
import com.buckwheat.garden.domain.plant.dto.projection.Calculate;
import com.buckwheat.garden.domain.watering.dto.ChemicalUsage;
import com.buckwheat.garden.domain.watering.dto.WateringResponse;
import com.buckwheat.garden.domain.watering.repository.WateringRepository;
import com.buckwheat.garden.global.code.WateringCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class GardenResponseMapper {
    private final WateringRepository wateringRepository;

    public GardenResponse getGardenResponse(Calculate calculate) {
        if (isPostponeDay(calculate)) {
            return new GardenResponse(calculate.getPlant(), GardenDetail.lazy(calculate.getLatestWateringDate(), calculate.getBirthday()));
        }

        return new GardenResponse(calculate.getPlant(), getGardenDetail(calculate));
    }

    public GardenDetail getGardenDetail(Calculate calculate) {
        // 물주기 기록이 없으면
        if (calculate.getLatestWateringDate() == null) {
            // 물주기 정보가 부족해요
            return GardenDetail.noRecord(calculate.getBirthday());
        }

        PlantResponse plant = calculate.getPlant();

        // 비료든 물이든 뭐라도 준지 며칠이나 지났는지 계산
        int wateringDDay = getWateringDDay(plant.getRecentWateringPeriod(), calculate.getLatestWateringDate());
        // 이 식물은 목이 말라요, 흙이 말랐는지 확인해보세요 ... 등의 watering code를 계산
        int wateringCode = getWateringCode(plant.getRecentWateringPeriod(), wateringDDay);

        // chemicalInfo: 물을 줄 식물에 대해서 맹물을 줄지 비료/약품 희석액을 줄지 알려주는 용도
        // 어떤 비료를 줘야하는지 알려준다
        ChemicalInfo chemicalInfo = getChemicalCode(plant.getId(), calculate.getGardenerId(), wateringCode);

        return GardenDetail.builder()
                .latestWateringDate(WateringResponse.from(calculate.getLatestWateringDate()))
                .anniversary(GardenDetail.getAnniversary(calculate.getBirthday()))
                .wateringDDay(wateringDDay)
                .wateringCode(wateringCode)
                .chemicalInfo(chemicalInfo)
                .build();
    }

    public int getWateringDDay(int recentWateringPeriod, LocalDate lastDrinkingDay) {
        // 비료든 물이든 뭐라도 준지 며칠이나 지났는지 계산
        int period = (int) Duration.between(lastDrinkingDay.atStartOfDay(), LocalDate.now().atStartOfDay()).toDays();
        return recentWateringPeriod - period;
    }

    public int getWateringCode(int recentWateringPeriod, int wateringDDay) {
        if (recentWateringPeriod == wateringDDay) {
            // 오늘 물 줌
            return WateringCode.WATERED_TODAY.getCode();
        } else if (recentWateringPeriod == 0) {
            // 물주기 정보 부족
            return WateringCode.NO_RECORD.getCode();
        } else if (wateringDDay == 0) {
            // 물주기
            return WateringCode.THIRSTY.getCode();
        } else if (wateringDDay == 1) {
            // 물주기 하루 전
            // 체크하세요
            return WateringCode.CHECK.getCode();
        } else if (wateringDDay >= 2) {
            // 물주기까지 이틀 이상 남음
            // 놔두세요
            return WateringCode.LEAVE_HER_ALONE.getCode();
        } else {
            // 음수가 나왔으면 물주기 놓침
            // 며칠 늦었는지 알려줌
            return wateringDDay;
        }
    }

    @Transactional(readOnly = true)
    public ChemicalInfo getChemicalCode(Long plantId, Long gardenerId, int wateringCode) {
        List<ChemicalUsage> latestChemicalUsages = wateringRepository.findLatestChemicalizedDayList(gardenerId, plantId, "Y");

        for (ChemicalUsage latestFertilizingInfo : latestChemicalUsages) {
            LocalDate latestFertilizedDate = latestFertilizingInfo.getLatestWateringDate();

            if (latestFertilizedDate != null) {
                int period = (int) Duration.between(latestFertilizedDate.atStartOfDay(), LocalDate.now().atStartOfDay()).toDays();
                if (period >= (int) latestFertilizingInfo.getPeriod()
                        && (wateringCode == WateringCode.THIRSTY.getCode() || wateringCode == WateringCode.CHECK.getCode())) {
                    return new ChemicalInfo(latestFertilizingInfo.getChemicalId(), latestFertilizingInfo.getName());
                }
            }
        }

        return null;
    }

    private boolean isPostponeDay(Calculate calculate) {
        return calculate.getPostponeDate() != null && LocalDate.now().isEqual(calculate.getPostponeDate());
    }
}
