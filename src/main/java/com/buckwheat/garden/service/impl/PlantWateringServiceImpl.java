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
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class PlantWateringServiceImpl implements PlantWateringService {
    private final WateringDao wateringDao;
    private final WateringUtil wateringUtil;

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
    public WateringDto.AfterWatering add(WateringDto.Request wateringRequest) {
        Watering watering = wateringDao.addWatering(wateringRequest);
        return getAfterWatering(watering.getPlant());
    }

    @Override
    public WateringDto.AfterWatering getAfterWatering(Plant plant){
        WateringDto.Message wateringMsg = wateringUtil.getWateringMsg(plant.getPlantId());

        // 리턴용 DTO 만들기
        List<WateringDto.ForOnePlant> waterings = getAll(plant.getPlantId());

        // 식물 테이블의 averageWateringDate 업데이트 필요 X
        if (wateringMsg.getAfterWateringCode() == 3) {
            return WateringDto.AfterWatering.from(wateringMsg, waterings);
        }

        // 필요시 물주기 정보 업데이트
        Plant newPlant = wateringUtil.updateWateringPeriod(plant, wateringMsg.getAverageWateringDate());

        return WateringDto.AfterWatering.from(PlantDto.Response.from(newPlant), wateringMsg, waterings);
    }

    @Override
    public List<WateringDto.ForOnePlant> getAll(Long plantId) {
        List<Watering> waterings = wateringDao.getWateringListByPlantId(plantId); // orderByWateringDateDesc

        // 며칠만에 물 줬는지도 계산해줌
        if (waterings.size() >= 2) {
            return withWateringPeriodList(waterings);
        }

        return waterings.stream().map(WateringDto.ForOnePlant::from).collect(Collectors.toList());
    }

    /**
     *
     * @param list wateringDate DESC로 정렬되어 있음
     * @return 며칠만에 물줬는지를 포함하는 DTO 리스트
     */
    @Override
    public List<WateringDto.ForOnePlant> withWateringPeriodList(List<Watering> list) {
        List<WateringDto.ForOnePlant> waterings = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            // 마지막 요소이자 가장 옛날 물주기
            // => 전 요소가 없으므로 며칠만에 물 줬는지 계산 X
            if (i == list.size() - 1) {
                waterings.add(WateringDto.ForOnePlant.from(list.get(i)));
                break;
            }

            // 며칠만에 물줬는지 계산
            LocalDateTime afterWateringDate = list.get(i).getWateringDate().atStartOfDay();
            LocalDateTime prevWateringDate = list.get(i + 1).getWateringDate().atStartOfDay();

            int wateringPeriod = (int) Duration.between(prevWateringDate, afterWateringDate).toDays();

            waterings.add(WateringDto.ForOnePlant.withWateringPeriodFrom(list.get(i), wateringPeriod));
        }

        return waterings;
    }

    @Override
    public WateringDto.AfterWatering modify(WateringDto.Request wateringRequest) {
        Watering watering = wateringDao.modifyWatering(wateringRequest);
        return getAfterWatering(watering.getPlant());
    }

    @Override
    public void delete(Long id) {
        wateringDao.deleteById(id);
    }


    @Override
    public void deleteAll(Long plantId) {
        wateringDao.deleteByPlantId(plantId);
    }
}
