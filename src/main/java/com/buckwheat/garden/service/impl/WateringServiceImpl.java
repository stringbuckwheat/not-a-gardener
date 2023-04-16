package com.buckwheat.garden.service.impl;

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
    private final WateringUtil wateringUtil;

    @Override
    public Map<LocalDate, List<WateringDto.ByDate>> getWateringList(int memberNo, int month) {
        // 1일이 일요일이면 뒤로 2주 더
        // 1일이 일요일이 아니면 앞으로 한 주 뒤로 한 주
        LocalDate firstDayOfMonth = LocalDate.of(2023, month, 1);

        // 월 화 수 목 금 토 일 => 1, 2, 3, 4, 5, 6, 7
        // 일요일이면 가만히 두고, 나머지 요일이면 getDayOfWeek().getValue()를 빼면
        // 7로 나눈 나머지를 뺌
        LocalDate startDate = firstDayOfMonth.minusDays(firstDayOfMonth.getDayOfWeek().getValue() % 7);
        log.debug("startDate: {}", startDate.toString());

        // end date: 합쳐서 42가 되게 만드는 값
        // x = 42 - 이번달 - startDate으로 더한 값
        int tmp = 42 - firstDayOfMonth.lengthOfMonth() - firstDayOfMonth.getDayOfWeek().getValue() % 7;
        log.debug("tmp: {}", tmp);
        LocalDate endDate = firstDayOfMonth.plusDays(firstDayOfMonth.lengthOfMonth() - 1 + tmp);
        log.debug("endDate: {}", endDate.toString());

        Map<LocalDate, List<WateringDto.ByDate>> map = new HashMap<>(); // 날짜: 리스트

        for (Watering watering : wateringDao.getAllWateringListByMemberNo(memberNo, startDate, endDate)) {
            List<WateringDto.ByDate> tmpList = map.get(watering.getWateringDate());

            if(tmpList == null){
                List<WateringDto.ByDate> list = new ArrayList<>();
                list.add(WateringDto.ByDate.from(watering));
                map.put(watering.getWateringDate(), list);
                continue;
            }

            tmpList.add(WateringDto.ByDate.from(watering));
            map.put(watering.getWateringDate(), tmpList);
        }

        log.debug("map: {}", map);
        return map;
    }

    public WateringDto.ByDate addWatering(WateringDto.Request wateringRequest){
        // 물주기 저장
        Watering watering = wateringDao.addWatering(wateringRequest);

        // 물주기 계산 로직
        WateringDto.Message wateringMsg = wateringUtil.getWateringMsg(watering.getPlant().getPlantNo());

        // 식물 테이블의 averageWateringDate 업데이트 필요 X
        if (wateringMsg.getAfterWateringCode() == 3) {
            // 바로 리턴
            return WateringDto.ByDate.from(watering, watering.getPlant(), watering.getChemical());
        }

        // 필요시 물주기 정보 업데이트
        Plant newPlant = wateringUtil.updateWateringPeriod(watering.getPlant(), wateringMsg.getAverageWateringDate());

        return WateringDto.ByDate.from(watering, newPlant, watering.getChemical());
    }
}
