package com.buckwheat.garden.service.impl;

import com.buckwheat.garden.data.dto.PlantDto;
import com.buckwheat.garden.data.entity.Plant;
import com.buckwheat.garden.repository.PlantRepository;
import com.buckwheat.garden.repository.WateringRepository;
import com.buckwheat.garden.service.GardenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class GardenServiceImpl implements GardenService {
    // 비료 주기는 일단 내가 하던대로 5일에 맞춰놓았다.
    // TODO 비료 주기 커스터마이징 기능
    private final int FERTILIZING_SCHEDULE = 5;

    private final PlantRepository plantRepository;
    private final WateringRepository wateringRepository;

    @Override
    public List<PlantDto> getPlantList(String username) {
        List<PlantDto> plantList = new ArrayList<>();

        for(Plant p : plantRepository.findByMember_Username(username)){
            PlantDto plantDto = new PlantDto(p);
            calculateCode(plantDto);

            plantList.add(plantDto);
        }

        return plantList;
    }

    @Override
    public LocalDate getLastDrinkingDay(int plantNo){
        LocalDate latestWateringDay = wateringRepository.findLatestWateringDayByPlantNo(plantNo);
        LocalDate latestFertilizedDay = wateringRepository.findLatestFertilizedDayByPlantNo(plantNo);

        if(latestWateringDay == null && latestFertilizedDay == null){
            return null;
        } else if(latestWateringDay != null && latestFertilizedDay == null){
            return latestWateringDay;
        } else if(latestWateringDay == null) {
            // 이 입력은 아직 불가능
            return latestFertilizedDay;
        }

        // 둘 중에 더 큰 날짜를 반환
        // true면 latestFertilizedDay가 더 큰 날짜
        LocalDate lastDrinkingDay = (latestFertilizedDay.isAfter(latestWateringDay)) ? latestFertilizedDay : latestWateringDay;

        return lastDrinkingDay;
    }

    // 비료줘야 하면 1, 안 줘도 되면 0
    @Override
    public int getFertilizingCode(int plantNo){
        LocalDate latestFertilizedDay = wateringRepository.findLatestFertilizedDayByPlantNo(plantNo);

        // 비료를 준 적이 없는 경우
        if(latestFertilizedDay == null){
            // 일단 맹물 주도록
            // TODO 첫 비료 스케줄 잡는 로직 추가
            return 0;
        }

        // 비료준지 얼마나 지났는지 계산
        int fertilizingSchedule = Period.between(latestFertilizedDay, LocalDate.now()).getDays();
        // log.debug("fertilizingSchedule: " + fertilizingSchedule);

        // 비료준 지 5일이 지났는지 확인하고 해당하는 코드 반환
        return (fertilizingSchedule - FERTILIZING_SCHEDULE >= 0) ? 1 : 0;
    }

    @Override
    public void calculateCode(PlantDto plantDto){
        int recentWateringPeriod = plantDto.getAverageWateringPeriod();
        LocalDate lastDrinkingDay = getLastDrinkingDay(plantDto.getPlantNo());

        // 물을 준 적이 한 번도 없는 경우
        if(lastDrinkingDay == null){
            plantDto.setWateringCode(4);
            plantDto.setFertilizingCode(0);

            return;
        }

        // 비료 줄지 말지 여부 계산
        int fertilizingCode = getFertilizingCode(plantDto.getPlantNo());

        // 비료든 물이든 뭐라도 준지 며칠이나 지났는지 계산
        // 관엽이라 한달 넘어갈 일은 없으므로 일만 계산
        int period = Period.between(lastDrinkingDay, LocalDate.now()).getDays();

        // 0이면 물 줄 날짜
        // 1이면 물 주기 하루 전이니까 체크해보기
        int wateringCode = recentWateringPeriod - period;

        // 음수가 나오면 물주기를 놓친 것이므로
        if(wateringCode < 0){
            wateringCode = 2;
            fertilizingCode = 0; // 비료 주기가 지났어도 비료 금지
        } else if (wateringCode > 2){
            // 3 이상이면... 딱히 할 일 없는 식물
            wateringCode = 3;
        }

        // 오늘 물 준 식물
        if(period == 0){
            wateringCode = 5;
        }

        plantDto.setWateringCode(wateringCode);
        plantDto.setFertilizingCode(fertilizingCode);

        log.debug("after calculate: " + plantDto);
    }
}
