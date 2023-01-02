package com.buckwheat.garden.service.impl;

import com.buckwheat.garden.dao.PlantDao;
import com.buckwheat.garden.dao.WateringDao;
import com.buckwheat.garden.data.dto.PlantDto;
import com.buckwheat.garden.data.entity.Plant;
import com.buckwheat.garden.service.GardenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class GardenServiceImpl implements GardenService {

    // 비료 주기는 일단 내가 하던대로 5일에 맞춰놓았다.
    // TODO 비료 주기 커스터마이징 기능
    private final int FERTILIZING_SCHEDULE = 5;
    @Autowired
    private PlantDao plantDao;

    @Autowired
    private WateringDao wateringDao;

    @Override
    public List<PlantDto> getPlantList(String id) {
        List<PlantDto> plantList = new ArrayList<>();

        for(Plant p : plantDao.getPlantListByUsername(id)){
            PlantDto plantDto = new PlantDto(p);
            calculateCode(plantDto);

            plantList.add(plantDto);
        }

        return plantList;
    }

    @Override
    public void calculateCode(PlantDto plantDto){
        int recentWateringPeriod = plantDto.getAverageWateringPeriod();
        LocalDate latestWateringDay = wateringDao.getLatestWateringDayByPlantNo(plantDto.getPlantNo());
        LocalDate latestFertilizedDay = wateringDao.getLatestFertilizedDayByPlantNo(plantDto.getPlantNo());
        LocalDate today = LocalDate.now();

        // 둘 중에 더 큰 날짜를 반환
        // true면 latestFertilizedDay가 더 큰 날짜
        LocalDate lastDrinkingDay = (latestFertilizedDay.isAfter(latestWateringDay)) ? latestFertilizedDay : latestWateringDay;

        // log.debug("today: " + today);
        // log.debug("lastDrinkingDay: " + lastDrinkingDay);
        // log.debug("Period.between(today, lastDrinkingDay): " + Period.between(today, lastDrinkingDay));

        // 비료든 물이든 뭐라도 준지 며칠이나 지났는지 계산
        // 관엽이라 한달 넘어갈 일은 없으므로 일만 계산
        int period = Period.between(lastDrinkingDay, today).getDays();

        // 비료준지 얼마나 지났는지 계산
        int fertilizingSchedule = Period.between(latestFertilizedDay, today).getDays();
        // log.debug("fertilizingSchedule: " + fertilizingSchedule);

        // 비료준 지 5일이 지났는지 확인하고 해당하는 코드 반환
        int fertilizingCode = (fertilizingSchedule - FERTILIZING_SCHEDULE >= 0) ? 1 : 0;

        // log.debug("recentWateringPeriod: " + recentWateringPeriod);
        // log.debug("period: " + period);

        // 0이면 물 줄 날짜
        // 1이면 물 주기 하루 전이니까 체크해보기
        int wateringCode = recentWateringPeriod - period;

        // 음수가 나오면 물주기를 놓친 것이므로
        if(wateringCode < 0){
            wateringCode = 2;
        } else if (wateringCode > 2){
            // 3 이상이면... 딱히 할 일 없는 식물
            wateringCode = 3;
        }

        plantDto.setWateringCode(wateringCode);
        plantDto.setFertilizingCode(fertilizingCode);

        log.debug("after calculate: " + plantDto);
    }
}
