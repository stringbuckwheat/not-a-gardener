package com.buckwheat.garden.util;

import com.buckwheat.garden.code.WateringCode;
import com.buckwheat.garden.dao.WateringDao;
import com.buckwheat.garden.data.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class GardenUtil {
    private final WateringDao wateringDao;

    public String getAnniversary(LocalDate birthday) {
        if (birthday == null) {
            return "";
        }

        LocalDate today = LocalDate.now();

        // 생일이면
        if (today.getMonth() == birthday.getMonth() && today.getDayOfMonth() == birthday.getDayOfMonth()) {
            return "생일 축하해요";
        }

        return Duration.between(birthday.atStartOfDay(), today.atStartOfDay()).toDays() + "일 째 반려중";
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
    public GardenDto.ChemicalCode getChemicalCode(Long plantId, Long gardenerId) {
        List<ChemicalUsage> latestChemicalUsages = wateringDao.getLatestChemicalUsages(plantId, gardenerId);

        // index 필요
        // chemical list index에 맞춰 해당 chemical을 줘야하는지 말아야하는지 산출
        for (int i = 0; i < latestChemicalUsages.size(); i++) {
            ChemicalUsage latestFertilizingInfo = latestChemicalUsages.get(i);
            LocalDate latestFertilizedDate = latestFertilizingInfo.getLatestWateringDate();

            // TODO 하이포넥스 14일, 개화용비료 14일, 물주기 14일이면 계속 하이포넥스만 걸림

            if (latestFertilizedDate == null) {
                // 해당 비료를 준 기록이 아예 없으면
                // TODO 제일 첫 비료 주기 알림은 최초 물주기 들고 와야함
                continue;
            }

            // 해당 비료를 준지 얼마나 지났는지 계산
            int period = (int) Duration.between(latestFertilizedDate.atStartOfDay(), LocalDate.now().atStartOfDay()).toDays();

            // 시비 날짜와 같거나 더 지났으면
            if (period >= (int) latestFertilizingInfo.getPeriod()) {
                return new GardenDto.ChemicalCode(latestFertilizingInfo.getChemicalId(), latestFertilizingInfo.getName());
            }
        }

        return null;
    }
}
