package com.buckwheat.garden.service.impl;

import com.buckwheat.garden.dao.PlantDao;
import com.buckwheat.garden.dao.WateringDao;
import com.buckwheat.garden.data.dto.WateringDto;
import com.buckwheat.garden.data.entity.Plant;
import com.buckwheat.garden.data.entity.Watering;
import com.buckwheat.garden.service.WateringService;
import com.buckwheat.garden.util.WateringUtil;
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
    private final PlantDao plantDao;
    private final WateringUtil wateringUtil;

    @Override
    public Map<LocalDate, List<WateringDto.ByDate>> getAll(Long gardenerId, LocalDate date) {
        // 1일이 일요일이면 뒤로 2주 더
        // 1일이 일요일이 아니면 앞으로 한 주 뒤로 한 주
        LocalDate firstDayOfMonth = LocalDate.of(date.getYear(), date.getMonth(), 1);
        LocalDate startDate = getStartDate(firstDayOfMonth);
        log.debug("startDate: {}", startDate);
        LocalDate endDate = getEndDate(firstDayOfMonth);
        log.debug("endDate: {}", endDate);

        Map<LocalDate, List<WateringDto.ByDate>> map = new HashMap<>(); // 날짜: 리스트

        for (Watering watering : wateringDao.getAllWateringListByGardenerId(gardenerId, startDate, endDate)) {
            List<WateringDto.ByDate> tmpList = map.get(watering.getWateringDate());

            if(tmpList == null){
                tmpList = new ArrayList<>();
            }

            tmpList.add(WateringDto.ByDate.from(watering));
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

    public WateringDto.ByDate add(WateringDto.Request wateringRequest){
        // 물주기 저장
        Watering watering = wateringDao.addWatering(wateringRequest);

        // 물주기 계산 로직
        Plant plant = plantDao.getPlantWithPlaceAndWatering(wateringRequest.getPlantId());
        WateringDto.Message wateringMsg = wateringUtil.getWateringMsg(plant);

        // 필요시 물주기 정보 업데이트
        plant = plantDao.updateWateringPeriod(watering.getPlant(), wateringMsg.getAverageWateringDate());

        return WateringDto.ByDate.from(watering, plant, watering.getChemical());
    }

    public void delete(long wateringId){
        wateringDao.deleteById(wateringId);
    }
}
