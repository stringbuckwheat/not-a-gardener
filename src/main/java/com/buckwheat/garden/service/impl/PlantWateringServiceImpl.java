package com.buckwheat.garden.service.impl;

import com.buckwheat.garden.dao.PlantDao;
import com.buckwheat.garden.dao.WateringDao;
import com.buckwheat.garden.data.dto.plant.PlantResponse;
import com.buckwheat.garden.data.dto.watering.AfterWatering;
import com.buckwheat.garden.data.dto.watering.WateringForOnePlant;
import com.buckwheat.garden.data.dto.watering.WateringMessage;
import com.buckwheat.garden.data.dto.watering.WateringRequest;
import com.buckwheat.garden.data.entity.Plant;
import com.buckwheat.garden.data.entity.Watering;
import com.buckwheat.garden.service.PlantWateringService;
import com.buckwheat.garden.util.WateringUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class PlantWateringServiceImpl implements PlantWateringService {
    private final WateringDao wateringDao;
    private final PlantDao plantDao;
    private final WateringUtil wateringUtil;

    /**
     * 물주기 기록 추가
     *
     * @param wateringRequest
     * @return WateringResponseDto
     */
    @Override
    public AfterWatering add(WateringRequest wateringRequest) {
        wateringDao.addWatering(wateringRequest);
        return getAfterWatering(wateringRequest.getPlantId());
    }

    @Override
    public AfterWatering getAfterWatering(Long plantId){
        // *****
        Plant plant = plantDao.getPlantWithPlaceAndWatering(plantId);
        WateringMessage wateringMsg = wateringUtil.getWateringMsg(plant);

        // 리턴용 DTO 만들기
        List<WateringForOnePlant> waterings = getAll(plant.getPlantId());
        Plant resPlant = plantDao.updateWateringPeriod(plant, wateringMsg.getAverageWateringDate());

        return AfterWatering.from(PlantResponse.from(resPlant), wateringMsg, waterings);
    }

    @Override
    public List<WateringForOnePlant> getAll(Long plantId) {
        List<Watering> waterings = wateringDao.getWateringListByPlantId(plantId); // orderByWateringDateDesc

        // 며칠만에 물 줬는지도 계산해줌
        if (waterings.size() >= 2) {
            return wateringUtil.withWateringPeriodList(waterings);
        }

        return waterings.stream().map(WateringForOnePlant::from).collect(Collectors.toList());
    }

    @Override
    public AfterWatering modify(WateringRequest wateringRequest) {
        Watering watering = wateringDao.modifyWatering(wateringRequest);
        log.debug("수정 후 watering: {}", watering);
        return getAfterWatering(wateringRequest.getPlantId());
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
