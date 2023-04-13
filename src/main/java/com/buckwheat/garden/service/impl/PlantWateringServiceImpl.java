package com.buckwheat.garden.service.impl;

import com.buckwheat.garden.dao.WateringDao;
import com.buckwheat.garden.data.dto.PlantDto;
import com.buckwheat.garden.data.dto.WateringDto;
import com.buckwheat.garden.data.entity.Plant;
import com.buckwheat.garden.data.entity.Watering;
import com.buckwheat.garden.service.PlantWateringService;
import com.buckwheat.garden.util.WateringUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PlantWateringServiceImpl implements PlantWateringService {
    private final WateringUtil wateringUtil;
    private final WateringDao wateringDao;

    /**
     * 이번 관수가 며칠만인지 계산
     *
     * @param latestWateringDate DB에 저장된 가장 최근 물주기
     * @return 이번 관수가 며칠만인지
     */
    public int calculateWateringPeriod(LocalDateTime latestWateringDate) {
        // 최근 물주기가 변경되었는지 확인하기
        // ex. 그간 나흘마다 물을 주다가 사흘만에 흙이 마르게 되었다.

        // 이번 관수가 며칠만인지
        LocalDateTime today = LocalDate.now().atStartOfDay();
        return (int) Duration.between(latestWateringDate, today).toDays();
    }

    /**
     * 물주기 기록 추가
     *
     * @param wateringRequest
     * @return WateringResponseDto
     */
    @Override
    public WateringDto.AfterWatering addWatering(WateringDto.Request wateringRequest) {
        Watering watering = wateringDao.addWatering(wateringRequest);
        return getAfterWatering(watering.getPlant());
    }

    @Override
    public WateringDto.AfterWatering getAfterWatering(Plant plant){
        WateringDto.WateringMsg wateringMsg = wateringUtil.getWateringMsg(plant.getPlantNo());

        // 리턴용 DTO 만들기
        List<WateringDto.WateringForOnePlant> wateringList = getWateringListForPlant(plant.getPlantNo());

        // 식물 테이블의 averageWateringDate 업데이트 필요 X
        if (wateringMsg.getAfterWateringCode() == 3) {
            return WateringDto.AfterWatering.from(wateringMsg, wateringList);
        }

        // 필요시 물주기 정보 업데이트
        Plant plantForAdd = wateringUtil.getPlantForAdd(plant, wateringMsg.getAverageWateringDate());

        return WateringDto.AfterWatering.from(PlantDto.PlantResponse.from(plantForAdd), wateringMsg, wateringList);
    }

    @Override
    public List<WateringDto.WateringForOnePlant> getWateringListForPlant(int plantNo) {
        List<Watering> list = wateringDao.getWateringListByPlantNo(plantNo); // orderByWateringDateDesc

        // 며칠만에 물 줬는지도 계산해줌
        if (list.size() >= 2) {
            return withWateringPeriodList(list);
        }

        List<WateringDto.WateringForOnePlant> wateringList = new ArrayList<>();

        for (Watering watering : list) {
            wateringList.add(
                    WateringDto.WateringForOnePlant.from(watering)
            );
        }

        return wateringList;
    }

    /**
     *
     * @param list wateringDate DESC로 정렬되어 있음
     * @return 며칠만에 물줬는지를 포함하는 DTO 리스트
     */
    @Override
    public List<WateringDto.WateringForOnePlant> withWateringPeriodList(List<Watering> list) {
        List<WateringDto.WateringForOnePlant> wateringList = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            // 마지막 요소이자 가장 옛날 물주기
            // => 전 요소가 없으므로 며칠만에 물 줬는지 계산 X
            if (i == list.size() - 1) {
                wateringList.add(
                        WateringDto.WateringForOnePlant.from(list.get(i))
                );

                break;
            }

            // 며칠만에 물줬는지 계산
            LocalDateTime afterWateringDate = list.get(i).getWateringDate().atStartOfDay();
            LocalDateTime prevWateringDate = list.get(i + 1).getWateringDate().atStartOfDay();

            int wateringPeriod = (int) Duration.between(prevWateringDate, afterWateringDate).toDays();

            wateringList.add(
                    WateringDto.WateringForOnePlant.withWateringPeriodFrom(list.get(i), wateringPeriod)
            );
        }

        return wateringList;
    }

    @Override
    public WateringDto.AfterWatering modifyWatering(WateringDto.Request wateringRequest) {
        Watering watering = wateringDao.modifyWatering(wateringRequest);
        return getAfterWatering(watering.getPlant());
    }

    @Override
    public void deleteWatering(int wateringNo) {
        wateringDao.removeWatering(wateringNo);
    }

    @Override
    public void deleteAllFromPlant(int plantNo) {
        wateringDao.removeAllWateringByPlantNo(plantNo);
    }
}
