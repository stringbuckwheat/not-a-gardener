package com.buckwheat.garden.service.impl;

import com.buckwheat.garden.code.WateringCode;
import com.buckwheat.garden.data.dto.Calculate;
import com.buckwheat.garden.data.dto.GardenDto;
import com.buckwheat.garden.data.dto.PlantDto;
import com.buckwheat.garden.data.dto.WateringDto;
import com.buckwheat.garden.util.GardenUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
@Slf4j
public class GardenResponseProvider {
    private final GardenUtil gardenUtil;

    public GardenDto.Response getGardenResponse(Calculate calculate){
        if (calculate.getPostponeDate() != null && LocalDate.now().compareTo(calculate.getPostponeDate()) == 0) {
            return new GardenDto.Response(calculate.getPlant(), getGardenDetailWhenLazy(calculate));
        }

        GardenDto.Detail gardenDetail = getGardenDetail(calculate);

        return new GardenDto.Response(calculate.getPlant(), gardenDetail);
    }

    public GardenDto.Detail getGardenDetailWhenLazy(Calculate calculate) {
        String anniversary = gardenUtil.getAnniversary(calculate.getBirthday());

        WateringDto.Response latestWatering = null;
        if(calculate.getLatestWateringDate() != null){
            latestWatering = WateringDto.Response.from(calculate.getLatestWateringDate());
        }

        return GardenDto.Detail.builder()
                .latestWateringDate(latestWatering)
                .anniversary(anniversary)
                .wateringDDay(calculate.getLatestWateringDate() == null ? -1 : 0)
                .wateringCode(WateringCode.YOU_ARE_LAZY.getCode())
                .chemicalCode(null)
                .build();
    }

    public GardenDto.Detail getGardenDetail(Calculate calculate) {
        // nn일째 반려중
        String anniversary = gardenUtil.getAnniversary(calculate.getBirthday());

        // 물주기 기록이 없으면
        if (calculate.getLatestWateringDate() == null) {
            // 물주기 정보가 부족해요
            return GardenDto.Detail.builder()
                    .latestWateringDate(null)
                    .anniversary(anniversary)
                    .wateringDDay(-1)
                    .wateringCode(WateringCode.NO_RECORD.getCode())
                    .chemicalCode(null)
                    .build();
        }

        PlantDto.Response plant = calculate.getPlant();

        // 비료든 물이든 뭐라도 준지 며칠이나 지났는지 계산
        int wateringDDay = gardenUtil.getWateringDDay(plant.getRecentWateringPeriod(), calculate.getLatestWateringDate());
        // 이 식물은 목이 말라요, 흙이 말랐는지 확인해보세요 ... 등의 watering code를 계산
        int wateringCode = gardenUtil.getWateringCode(plant.getRecentWateringPeriod(), wateringDDay);

        // chemicalCode: 물을 줄 식물에 대해서 맹물을 줄지 비료/약품 희석액을 줄지 알려주는 용도
        // 어떤 비료를 줘야하는지 알려준다
        GardenDto.ChemicalCode chemicalCode = null;

        if(wateringCode == 0 || wateringCode == 1){
            chemicalCode = gardenUtil.getChemicalCode(plant.getId(), calculate.getGardenerId());
        }

        return GardenDto.Detail.builder()
                .latestWateringDate(WateringDto.Response.from(calculate.getLatestWateringDate()))
                .anniversary(anniversary)
                .wateringDDay(wateringDDay)
                .wateringCode(wateringCode)
                .chemicalCode(chemicalCode).build();
    }
}
