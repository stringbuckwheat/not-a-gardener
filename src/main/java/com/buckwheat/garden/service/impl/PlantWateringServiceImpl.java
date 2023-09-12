package com.buckwheat.garden.service.impl;

import com.buckwheat.garden.dao.PlantDao;
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
    public WateringDto.AfterWatering add(WateringDto.Request wateringRequest) {
        wateringDao.addWatering(wateringRequest);
        return getAfterWatering(wateringRequest.getPlantId());
    }

    @Override
    public WateringDto.AfterWatering getAfterWatering(Long plantId){
        // *****
        Plant plant = plantDao.getPlantWithPlaceAndWatering(plantId);
        WateringDto.Message wateringMsg = wateringUtil.getWateringMsg(plant);

        // 리턴용 DTO 만들기
        List<WateringDto.ForOnePlant> waterings = getAll(plant.getPlantId());
        Plant resPlant = plantDao.updateWateringPeriod(plant, wateringMsg.getAverageWateringDate());

        return WateringDto.AfterWatering.from(PlantDto.Response.from(resPlant), wateringMsg, waterings);
    }

    @Override
    public List<WateringDto.ForOnePlant> getAll(Long plantId) {
        List<Watering> waterings = wateringDao.getWateringListByPlantId(plantId); // orderByWateringDateDesc

        // 며칠만에 물 줬는지도 계산해줌
        if (waterings.size() >= 2) {
            return wateringUtil.withWateringPeriodList(waterings);
        }

        return waterings.stream().map(WateringDto.ForOnePlant::from).collect(Collectors.toList());
    }

    @Override
    public WateringDto.AfterWatering modify(WateringDto.Request wateringRequest) {
        Watering watering = wateringDao.modifyWatering(wateringRequest);
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
