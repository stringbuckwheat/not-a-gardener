package com.buckwheat.garden.util;

import com.buckwheat.garden.code.WateringCode;
import com.buckwheat.garden.data.dto.ChemicalUsage;
import com.buckwheat.garden.data.dto.GardenDto;
import com.buckwheat.garden.data.dto.WateringDto;
import com.buckwheat.garden.data.entity.Plant;
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
    public GardenDto.Detail getGardenDetailWhenLazy(Plant plant){
        String anniversary = getAnniversary(plant.getBirthday());

        // 물주기 기록이 없으면
        if (plant.getWaterings().size() == 0) {
            return GardenDto.Detail.builder()
                    .latestWateringDate(null)
                    .anniversary(anniversary)
                    .wateringDDay(-1)
                    .wateringCode(WateringCode.NO_RECORD.getCode())
                    .chemicalCode(null)
                    .build();
            // return GardenDto.Detail.from(null, anniversary, -1, WateringCode.NO_RECORD.getCode(), null);
        }

        WateringDto.Response latestWatering = WateringDto.Response.from(plant.getWaterings().get(0));

        return GardenDto.Detail.builder()
                .latestWateringDate(latestWatering)
                .anniversary(anniversary)
                .wateringDDay(0)
                .wateringCode(WateringCode.YOU_ARE_LAZY.getCode())
                .chemicalCode(null)
                .build();

        // return GardenDto.Detail.from(latestWatering, anniversary, 0, WateringCode.YOU_ARE_LAZY.getCode(), null);
    }

    public GardenDto.Detail getGardenDetail(Plant plant, List<ChemicalUsage> latestChemicalUsages) {
        String anniversary = getAnniversary(plant.getBirthday());

        // 물주기 기록이 없으면
        if (plant.getWaterings() == null || plant.getWaterings().size() == 0) {
            return GardenDto.Detail.from(null, anniversary, -1, WateringCode.NO_RECORD.getCode(), null);
        }

        WateringDto.Response latestWatering = WateringDto.Response.from(plant.getWaterings().get(0));

        int wateringDDay = getWateringDDay(plant.getRecentWateringPeriod(), plant.getWaterings().get(0).getDate());
        int wateringCode = getWateringCode(plant.getRecentWateringPeriod(), wateringDDay);

        // chemicalCode: 물을 줄 식물에 대해서 맹물을 줄지 비료/약품 희석액을 줄지 알려주는 용도
        GardenDto.ChemicalCode chemicalCode = getChemicalCode(latestChemicalUsages);

        return GardenDto.Detail.from(latestWatering, anniversary, wateringDDay, wateringCode, chemicalCode);
    }

    public GardenDto.Detail getGardenDetail(Plant plant, int wateringDDay, int wateringCode, List<ChemicalUsage> latestChemicalUsages) {
        String anniversary = getAnniversary(plant.getBirthday());

        WateringDto.Response latestWatering = null;

        if (plant.getWaterings().size() > 0) {
            latestWatering = WateringDto.Response.from(plant.getWaterings().get(0));
        }

        // chemicalCode: 물을 줄 식물에 대해서 맹물을 줄지 비료/약품 희석액을 줄지 알려주는 용도
        GardenDto.ChemicalCode chemicalCode = getChemicalCode(latestChemicalUsages); // FertilizerNo or -1

        return GardenDto.Detail.from(latestWatering, anniversary, wateringDDay, wateringCode, chemicalCode);
    }
    
    public String getAnniversary(LocalDate birthday) {
        if(birthday == null){
            return "";
        }

        LocalDate today = LocalDate.now();

        // 생일이면
        if ((today.getMonth() == birthday.getMonth()) && (today.getDayOfMonth() == birthday.getDayOfMonth())) {
            return "생일 축하해요";
        }

        return Duration.between(birthday.atStartOfDay(), today.atStartOfDay()).toDays() + "일 째 반려중";
    }

    /**
     * 물주기 정보가 있으면 제일 최근에 물 준 날짜, 없으면 createDate
     *
     * @param plant
     * @return
     */
    public LocalDate getLastDrinkingDay(Plant plant) {
        // 물주기 정보가 있으면 진짜 제일 최근 물 준 날짜를 리턴
        if (plant.getWaterings().size() != 0) {
            return plant.getWaterings().get(0).getDate();
        }

        return plant.getCreateDate().toLocalDate();
    }

    public int getWateringDDay(int recentWateringPeriod, LocalDate lastDrinkingDay) {
        // 비료든 물이든 뭐라도 준지 며칠이나 지났는지 계산
        int period = (int) Duration.between(lastDrinkingDay.atStartOfDay(), LocalDate.now().atStartOfDay()).toDays();
        return recentWateringPeriod - period;
    }

    public int getWateringCode(int recentWateringPeriod, int wateringDDay) {
        if (recentWateringPeriod == 0) {
            // 물주기 정보 부족
            return WateringCode.NO_RECORD.getCode();
        } else if (wateringDDay == 0) {
            // 물주기
            return WateringCode.THIRSTY.getCode();
        } else if (wateringDDay == 1) {
            // 물주기 하루 전
            // 체크하세요
            return WateringCode.CHECK.getCode();
        } else if (recentWateringPeriod == wateringDDay) {
            // 오늘 물 줌
            return WateringCode.WATERED_TODAY.getCode();
        } else if (wateringDDay >= 2) { // 얘가 wateringCode == 4 보다 먼저 걸린다
            // 물주기까지 이틀 이상 남음
            // 놔두세요
            return WateringCode.LEAVE_HER_ALONE.getCode();
        } else {
            // 음수가 나왔으면 물주기 놓침
            // 며칠 늦었는지 알려줌
            return wateringDDay;
        }
    }

    // -1           0           1
    // 비료 사용 안함  맹물 주기      비료주기
    public GardenDto.ChemicalCode getChemicalCode(List<ChemicalUsage> latestChemicalUsages) {
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
            if (period >= (int) latestFertilizingInfo.getChemicalPeriod()) {
                return new GardenDto.ChemicalCode(latestFertilizingInfo.getChemicalId(), latestFertilizingInfo.getChemicalName());
            }
        }

        return null;
    }
}
