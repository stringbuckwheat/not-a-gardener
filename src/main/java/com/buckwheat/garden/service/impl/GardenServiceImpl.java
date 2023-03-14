package com.buckwheat.garden.service.impl;

import com.buckwheat.garden.data.dto.GardenDto;
import com.buckwheat.garden.data.entity.Fertilizer;
import com.buckwheat.garden.data.entity.Plant;
import com.buckwheat.garden.data.entity.Watering;
import com.buckwheat.garden.repository.FertilizerRepository;
import com.buckwheat.garden.repository.PlantRepository;
import com.buckwheat.garden.repository.WateringRepository;
import com.buckwheat.garden.service.GardenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class GardenServiceImpl implements GardenService {
    private final PlantRepository plantRepository;
    private final WateringRepository wateringRepository;
    private final FertilizerRepository fertilizerRepository;

    @Override
    public List<GardenDto> getGarden(int memberNo) {
        List<GardenDto> gardenList = new ArrayList<>();

        List<Fertilizer> fertilizerList = fertilizerRepository.findByMember_memberNo(memberNo);
        List<Plant> plantList = plantRepository.findByMember_MemberNo(memberNo);

        // 필요한 것들 계산해서 gardenDto list 리턴
        for (Plant plant : plantList) {
            List<Watering> wateringList = plant.getWateringList();
            String anniversary = "";

            // 키운 지 며칠이나 지났는지 (nullable)
            if (plant.getBirthday() != null) {
                anniversary = getAnniversary(plant.getBirthday());
//                log.debug("{}: {}", plant.getPlantName(), anniversary);
            }

            // watering code: 계산 값에 따라 화면에서 물주기, 물줄 날짜 놓침 등의 메시지를 띄울 용도
            // 물을 준 적이 한 번도 없는 경우
            int wateringDDay = getWateringDDay(plant.getAverageWateringPeriod(), getLastDrinkingDay(plant));
//            log.debug("{}의 wateringDDay: {}일 남음", plant.getPlantName(), wateringDDay);

            int wateringCode = getWateringCode(plant.getAverageWateringPeriod(), wateringDDay);
//            log.debug("{}의 watering code: {}", plant.getPlantName(), wateringCode);

            // TODO 비료 계산
            // fertilizingCode: 물을 줄 식물에 대해서 맹물을 줄지 비료를 줄지 알려주는 용도

            gardenList.add(
                    GardenDto.from(plant, anniversary, wateringDDay, wateringCode)
            );
        }

        // TODO 정렬
        log.debug("gardenList: {}", gardenList);
        return gardenList;
    }

    private String getAnniversary(LocalDate birthday) {
        LocalDate today = LocalDate.now();
        // 생일이면
        if ((today.getMonth() == birthday.getMonth()) && (today.getDayOfMonth() == birthday.getDayOfMonth())) {
            return "생일 축하해요";
        }

        return Duration.between(birthday.atStartOfDay(), today.atStartOfDay()).toDays() + "일 째 반려중";
    }

    private LocalDate getLastDrinkingDay(Plant plant) {
        // 물주기 정보가 있으면 진짜 제일 최근 물 준 날짜를 리턴
        if (plant.getWateringList().size() != 0) {
            return plant.getWateringList().get(0).getWateringDate();
        }

        return plant.getCreateDate().toLocalDate();
    }


    private int getWateringDDay(int recentWateringPeriod, LocalDate lastDrinkingDay) {
        // 비료든 물이든 뭐라도 준지 며칠이나 지났는지 계산
        int period = (int) Duration.between(lastDrinkingDay.atStartOfDay(), LocalDate.now().atStartOfDay()).toDays();
        return recentWateringPeriod - period;
    }

    private int getWateringCode(int recentWateringPeriod, int wateringDDay) {
        // 음수           0             1          2         3         4
        // 물주기 놓침   물주기 정보 부족    물주기     체크하기     놔두세요    오늘 물 줌

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
            wateringCode = 4;
        } else if (wateringDDay >= 2) { // 얘가 wateringCode == 4 보다 먼저 걸린다
            // 물주기까지 이틀 이상 남음
            // 놔두세요
            wateringCode = 3;
        } else if (wateringDDay < 0) {

            // 음수가 나왔으면 물주기 놓침
            // 며칠 늦었는지 알려줌
            wateringCode = wateringDDay;
        }

        return wateringCode;
    }

    // -1           0           1
    // 비료 사용 안함  맹물 주기
    public int getFertilizingCode(List<Fertilizer> fertilizerList, int plantNo) {
        LocalDate latestFertilizedDay = wateringRepository.findLatestFertilizedDayByPlantNo(plantNo);

        // 비료를 준 적이 없는 경우
        if (latestFertilizedDay == null) {
            // 일단 맹물 주도록
            // TODO 첫 비료 스케줄 잡는 로직 추가
            return 0;
        }

        // 비료준지 얼마나 지났는지 계산
        int fertilizingSchedule = Period.between(latestFertilizedDay, LocalDate.now()).getDays();
        // log.debug("fertilizingSchedule: " + fertilizingSchedule);

        // 비료준 지 5일이 지났는지 확인하고 해당하는 코드 반환
        return 0;
    }

}
