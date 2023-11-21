package com.buckwheat.garden.domain.plant.service;

import com.buckwheat.garden.domain.plant.dto.projection.Calculate;
import com.buckwheat.garden.domain.plant.dto.projection.ChemicalUsage;
import com.buckwheat.garden.global.code.WateringCode;
import com.buckwheat.garden.domain.plant.dto.garden.ChemicalCode;
import com.buckwheat.garden.domain.plant.dto.garden.GardenDetail;
import com.buckwheat.garden.domain.plant.dto.garden.GardenResponse;
import com.buckwheat.garden.domain.plant.dto.plant.PlantResponse;
import com.buckwheat.garden.domain.watering.repository.WateringRepository;
import com.buckwheat.garden.domain.watering.dto.WateringResponse;
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
public class GardenResponseProvider {
    private final WateringRepository wateringRepository;

    public GardenResponse getGardenResponse(Calculate calculate) {
        // 미루기를 누른 경우
        if (calculate.getPostponeDate() != null
                && LocalDate.now().compareTo(calculate.getPostponeDate()) == 0) {
            GardenDetail detail = GardenDetail.lazy(calculate.getLatestWateringDate(), calculate.getBirthday());
            return new GardenResponse(calculate.getPlant(), detail);
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

        // chemicalCode: 물을 줄 식물에 대해서 맹물을 줄지 비료/약품 희석액을 줄지 알려주는 용도
        // 어떤 비료를 줘야하는지 알려준다
        ChemicalCode chemicalCode = null;

        if (wateringCode == 0 || wateringCode == 1) {
            chemicalCode = getChemicalCode(plant.getId(), calculate.getGardenerId());
        }

        return GardenDetail.builder()
                .latestWateringDate(WateringResponse.from(calculate.getLatestWateringDate()))
                .anniversary(GardenDetail.getAnniversary(calculate.getBirthday()))
                .wateringDDay(wateringDDay)
                .wateringCode(wateringCode)
                .chemicalCode(chemicalCode).build();
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
            return WateringCode.NO_RECORD.getCode(); // ***
        } else if (wateringDDay == 0) {
            // 물주기
            return WateringCode.THIRSTY.getCode(); // ***
        } else if (wateringDDay == 1) {
            // 물주기 하루 전
            // 체크하세요
            return WateringCode.CHECK.getCode(); // ***
        } else if (wateringDDay >= 2) { // 얘가 wateringCode == 4 보다 먼저 걸린다
            // 물주기까지 이틀 이상 남음
            // 놔두세요
            return WateringCode.LEAVE_HER_ALONE.getCode();
        } else {
            // 음수가 나왔으면 물주기 놓침
            // 며칠 늦었는지 알려줌
            return wateringDDay; // ***
        }
    }

    // -1           0           1
    // 비료 사용 안함  맹물 주기      비료주기
    @Transactional(readOnly = true)
    public ChemicalCode getChemicalCode(Long plantId, Long gardenerId) {
        List<ChemicalUsage> latestChemicalUsages = wateringRepository.findLatestChemicalizedDayList(gardenerId, plantId, "Y");

        // index 필요
        // chemical list index에 맞춰 해당 chemical을 줘야하는지 말아야하는지 산출
        for (int i = 0; i < latestChemicalUsages.size(); i++) {
            ChemicalUsage latestFertilizingInfo = latestChemicalUsages.get(i);
            LocalDate latestFertilizedDate = latestFertilizingInfo.getLatestWateringDate();

            if (latestFertilizedDate == null) {
                // 해당 비료를 준 기록이 아예 없으면
                continue;
            }

            // 해당 비료를 준지 얼마나 지났는지 계산
            int period = (int) Duration.between(latestFertilizedDate.atStartOfDay(), LocalDate.now().atStartOfDay()).toDays();

            // 시비 날짜와 같거나 더 지났으면
            if (period >= (int) latestFertilizingInfo.getPeriod()) {
                return new ChemicalCode(latestFertilizingInfo.getChemicalId(), latestFertilizingInfo.getName());
            }
        }

        return null;
    }
}
