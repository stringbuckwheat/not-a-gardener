package com.buckwheat.garden.service.impl;

import com.buckwheat.garden.dao.WateringDao;
import com.buckwheat.garden.data.dto.watering.AfterWatering;
import com.buckwheat.garden.data.dto.watering.WateringByDate;
import com.buckwheat.garden.data.dto.watering.WateringRequest;
import com.buckwheat.garden.data.entity.Plant;
import com.buckwheat.garden.data.entity.Watering;
import com.buckwheat.garden.service.WateringService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class WateringServiceImpl implements WateringService {
    private final WateringDao wateringDao;

    @Override
    public Map<LocalDate, List<WateringByDate>> getAll(Long gardenerId, LocalDate date) {
        // 1일이 일요일이면 뒤로 2주 더
        // 1일이 일요일이 아니면 앞으로 한 주 뒤로 한 주
        LocalDate firstDayOfMonth = LocalDate.of(date.getYear(), date.getMonth(), 1);
        LocalDate startDate = getStartDate(firstDayOfMonth);
        LocalDate endDate = getEndDate(firstDayOfMonth);

        Map<LocalDate, List<WateringByDate>> map = new HashMap<>(); // 날짜: 리스트

        for (Watering watering : wateringDao.getAllWateringListByGardenerId(gardenerId, startDate, endDate)) {
            List<WateringByDate> tmpList = map.get(watering.getWateringDate());

            if(tmpList == null){
                tmpList = new ArrayList<>();
            }

            tmpList.add(WateringByDate.from(watering));
            map.put(watering.getWateringDate(), tmpList);
        }

        return map;
    }

    @Override
    // 달력 시작 날짜
    public LocalDate getStartDate(LocalDate firstDayOfMonth){
        // 월 화 수 목 금 토 일 => 1, 2, 3, 4, 5, 6, 7
        // 일요일이면 가만히 두고, 나머지 요일이면 getDayOfWeek().getValue()를 빼면
        // 7로 나눈 나머지 + 1을 뺌
        return firstDayOfMonth.minusDays(firstDayOfMonth.getDayOfWeek().getValue() % 7 + 1);
    }

    @Override
    // 달력 마지막 날짜
    public LocalDate getEndDate(LocalDate firstDayOfMonth){
        // end date: 합쳐서 42가 되게 만드는 값
        // x = 42 - 이번달 - startDate으로 더한 값
        int tmp = 42 - firstDayOfMonth.lengthOfMonth() - firstDayOfMonth.getDayOfWeek().getValue() % 7;
        return firstDayOfMonth.plusDays(firstDayOfMonth.lengthOfMonth() + tmp);
    }

    public WateringByDate add(WateringRequest wateringRequest){
        // 물주기 저장
        AfterWatering afterWatering = wateringDao.addWatering(wateringRequest);

        Plant plant = afterWatering.getPlant();
        Watering latestWatering = plant.getWaterings().get(0);

        return WateringByDate.from(latestWatering, plant, latestWatering.getChemical());
    }

    public void delete(Long wateringId, Long plantId, Long gardenerId){
        wateringDao.deleteById(wateringId, plantId, gardenerId);
    }
}
