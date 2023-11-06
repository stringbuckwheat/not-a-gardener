package com.buckwheat.garden.service.impl;

import com.buckwheat.garden.code.WateringCode;
import com.buckwheat.garden.dao.PlantDao;
import com.buckwheat.garden.dao.WateringDao;
import com.buckwheat.garden.data.dto.garden.GardenResponse;
import com.buckwheat.garden.data.dto.garden.GardenWateringResponse;
import com.buckwheat.garden.data.dto.watering.AfterWatering;
import com.buckwheat.garden.data.dto.watering.WateringMessage;
import com.buckwheat.garden.data.dto.watering.WateringRequest;
import com.buckwheat.garden.data.entity.Plant;
import com.buckwheat.garden.data.projection.Calculate;
import com.buckwheat.garden.service.GardenWateringService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;

@Service
@Slf4j
@RequiredArgsConstructor
public class GardenWateringServiceImpl implements GardenWateringService {
    private final WateringDao wateringDao;
    private final PlantDao plantDao;
    private final GardenResponseProvider gardenResponseProvider;

    @Override
    public GardenWateringResponse add(Long gardenerId, WateringRequest wateringRequest) {
        AfterWatering afterWatering = wateringDao.addWatering(wateringRequest);
        GardenResponse gardenResponse = gardenResponseProvider.getGardenResponse(Calculate.from(afterWatering.getPlant(), gardenerId));

        return new GardenWateringResponse(gardenResponse, afterWatering.getWateringMessage());
    }

    @Override
    @Transactional
    public WateringMessage notDry(Long plantId) {
        // 리턴용
        WateringMessage wateringMsg = null;

        Plant plant = plantDao.getPlantWithPlaceAndWatering(plantId);

        // 한 번도 물 준 적 없는 경우
        if (plant.getWaterings().size() == 0) {
            plantDao.updateConditionDate(plant);
            return null;
        }

        // averageWateringPeriod 안 마른 날짜만큼 업데이트
        // 마지막으로 물 준 날짜와 오늘과의 날짜 사이를 계산함
        LocalDate lastDrinkingDate = plant.getWaterings().get(0).getWateringDate();
        int period = (int) Duration.between(lastDrinkingDate.atStartOfDay(), LocalDate.now().atStartOfDay()).toDays();

        if (period + 1 == plant.getRecentWateringPeriod()) {
            // 하루 전에 체크하라고 카드 띄웠는데 not Dry를 누른 경우 -> averageWateringPeriod 변경할 필요 없음
            // 오늘 한정 garden에 안 뜨게 해야함 => conditionDate 오늘 날짜 추가
            plantDao.updateConditionDate(plant);

            // wateringMsg: 물주기 계산에 변동 없음
            wateringMsg = new WateringMessage(0, period);
        } else if (period >= plant.getRecentWateringPeriod()) {
            // averageWateringPeriod 업데이트
            // 오늘 한정 garden에 안 뜨게 해야함 => updateDate에 오늘 날짜 추가
            plant.updateRecentWateringPeriod(period + 1);
            plant.updateConditionDate();

            // 물주기 늘어나는 중이라는 wateringMsg 만들기
            wateringMsg = new WateringMessage(1, period + 1);
        }

        return wateringMsg;
    }

    @Override
    @Transactional
    public int postpone(Long plantId) {
        Plant plant = plantDao.getPlantWithPlaceAndWatering(plantId);
        // 미룰래요(그냥 귀찮아서 물주기 미룬 경우) == averageWateringPeriod 업데이트 안함!!
        // postponeDate를 업데이트함
        plant.updatePostponeDate();

        // waitingList에서는 오늘 하루만 없애줌
        return WateringCode.YOU_ARE_LAZY.getCode();
    }
}
