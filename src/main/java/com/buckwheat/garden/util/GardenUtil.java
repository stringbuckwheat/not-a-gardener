package com.buckwheat.garden.util;

import com.buckwheat.garden.data.dto.FertilizingInfo;
import com.buckwheat.garden.data.dto.GardenDto;
import com.buckwheat.garden.data.dto.WateringDto;
import com.buckwheat.garden.data.entity.Plant;
import com.buckwheat.garden.repository.WateringRepository;
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
    private final WateringRepository wateringRepository;

    public GardenDto.GardenDetail getGardenDetail(Plant plant, int memberNo){
        WateringDto.WateringResponse latestWatering = null;
        String anniversary = "";

        if(plant.getWateringList().size() > 0) {
            latestWatering = WateringDto.WateringResponse.from(plant.getWateringList().get(0));
        }

        // 키운 지 며칠이나 지났는지 (nullable)
        if (plant.getBirthday() != null) {
            anniversary = getAnniversary(plant.getBirthday());
        }

        // watering code: 계산 값에 따라 화면에서 물주기, 물줄 날짜 놓침 등의 메시지를 띄울 용도
        int wateringDDay = getWateringDDay(plant.getAverageWateringPeriod(), getLastDrinkingDay(plant));
        int wateringCode = getWateringCode(plant.getAverageWateringPeriod(), wateringDDay);

        // chemicalCode: 물을 줄 식물에 대해서 맹물을 줄지 비료/약품 희석액을 줄지 알려주는 용도
        GardenDto.ChemicalCode chemicalCode = getChemicalCode(plant.getPlantNo(), memberNo); // FertilizerNo or -1

        return GardenDto.GardenDetail.from(latestWatering, anniversary, wateringDDay, wateringCode, chemicalCode);
    }

    public GardenDto.GardenDetail getGardenDetail(Plant plant, int memberNo, int wateringDDay, int wateringCode){
        WateringDto.WateringResponse latestWatering = null;
        String anniversary = "";

        if(plant.getWateringList().size() > 0) {
            latestWatering = WateringDto.WateringResponse.from(plant.getWateringList().get(0));
        }

        if (plant.getBirthday() != null) {
            anniversary = getAnniversary(plant.getBirthday());
        }

        // chemicalCode: 물을 줄 식물에 대해서 맹물을 줄지 비료/약품 희석액을 줄지 알려주는 용도
        GardenDto.ChemicalCode chemicalCode = getChemicalCode(plant.getPlantNo(), memberNo); // FertilizerNo or -1

        return GardenDto.GardenDetail.from(latestWatering, anniversary, wateringDDay, wateringCode, chemicalCode);
    }

    public String getAnniversary(LocalDate birthday) {
        LocalDate today = LocalDate.now();
        // 생일이면
        if ((today.getMonth() == birthday.getMonth()) && (today.getDayOfMonth() == birthday.getDayOfMonth())) {
            return "생일 축하해요";
        }

        return Duration.between(birthday.atStartOfDay(), today.atStartOfDay()).toDays() + "일 째 반려중";
    }

    public LocalDate getLastDrinkingDay(Plant plant) {
        // 물주기 정보가 있으면 진짜 제일 최근 물 준 날짜를 리턴
        if (plant.getWateringList().size() != 0) {
            return plant.getWateringList().get(0).getWateringDate();
        }

        return plant.getCreateDate().toLocalDate();
    }

    public int getWateringDDay(int recentWateringPeriod, LocalDate lastDrinkingDay) {
        // 비료든 물이든 뭐라도 준지 며칠이나 지났는지 계산
        int period = (int) Duration.between(lastDrinkingDay.atStartOfDay(), LocalDate.now().atStartOfDay()).toDays();
        return recentWateringPeriod - period;
    }

    public int getWateringCode(int recentWateringPeriod, int wateringDDay) {
        // 음수  물주기 놓침
        // 0    물주기 정보 부족
        // 1    물주기
        // 2    체크하기
        // 3    물주기 늘어나는 중
        // 4    놔두세요
        // 5    오늘 물 줌
        // 6    하루 미루기

        int wateringCode = 999;

        if (recentWateringPeriod == 0) {
            // 물주기 정보 부족
            wateringCode = 0;
        } else if (wateringDDay == 0) {
            // 물주기
            wateringCode = 1;
        } else if (wateringDDay == 1) {
            // 물주기 하루 전
            // 체크하세요
            wateringCode = 2;
        } else if(recentWateringPeriod == wateringDDay){
            // 오늘 물 줌
            wateringCode = 5;
        } else if (wateringDDay >= 2) { // 얘가 wateringCode == 4 보다 먼저 걸린다
            // 물주기까지 이틀 이상 남음
            // 놔두세요
            wateringCode = 4;
        } else if (wateringDDay < 0) {
            // 음수가 나왔으면 물주기 놓침
            // 며칠 늦었는지 알려줌
            wateringCode = wateringDDay;
        }

        return wateringCode;
    }

    // -1           0           1
    // 비료 사용 안함  맹물 주기      chemicalNo
    public GardenDto.ChemicalCode getChemicalCode(int plantNo, int memberNo) {
        List<FertilizingInfo> latestFertilizedDateList = wateringRepository.findLatestChemicalizedDayList(plantNo, memberNo);

        // index 필요
        // chemical list index에 맞춰 해당 chemical을 줘야하는지 말아야하는지 산출
        for(int i = 0; i < latestFertilizedDateList.size(); i++){
            FertilizingInfo latestFertilizingInfo = latestFertilizedDateList.get(i);
            LocalDate latestFertilizedDate = latestFertilizingInfo.getLatestWateringDate();

            if(latestFertilizedDate == null){
                // 해당 비료를 준 기록이 아예 없으면
                // TODO 제일 첫 비료 주기 알림은 어떻게 할지 고민해볼 것
                continue;
            }

            // 해당 비료를 준지 얼마나 지났는지 계산
            int period = (int) Duration.between(latestFertilizedDate.atStartOfDay(), LocalDate.now().atStartOfDay()).toDays();

            // 시비 날짜와 같거나 더 지났으면
            if(period >= (int) latestFertilizingInfo.getChemicalPeriod()){
                return new GardenDto.ChemicalCode(latestFertilizingInfo.getChemicalNo(), latestFertilizingInfo.getChemicalName());
            }
        }

        return null;
    }
}
