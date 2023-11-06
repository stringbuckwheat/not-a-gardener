package com.buckwheat.garden.service.impl;

import com.buckwheat.garden.repository.command.WateringCommandRepository;
import com.buckwheat.garden.data.dto.plant.PlantResponse;
import com.buckwheat.garden.data.dto.watering.*;
import com.buckwheat.garden.data.entity.Plant;
import com.buckwheat.garden.data.entity.Watering;
import com.buckwheat.garden.repository.WateringRepository;
import com.buckwheat.garden.service.PlantWateringService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class PlantWateringServiceImpl implements PlantWateringService {
    private final WateringCommandRepository wateringDao;
    private final WateringRepository wateringRepository;

    @Override
    public PlantWateringResponse add(WateringRequest wateringRequest, Pageable pageable) {
        AfterWatering afterWatering = wateringDao.add(wateringRequest);

        List<WateringForOnePlant> waterings = getAll(wateringRequest.getPlantId(), pageable);

        return PlantWateringResponse.from(PlantResponse.from(afterWatering.getPlant()), afterWatering.getWateringMessage(), waterings);
    }

    @Override
    public List<WateringForOnePlant> getAll(Long plantId, Pageable pageable) {
        List<Watering> waterings = wateringRepository.findWateringsByPlantIdWithPage(plantId, pageable); // orderByWateringDateDesc

        // 며칠만에 물 줬는지
        if (waterings.size() >= 2) {
            return withWateringPeriodList(waterings);
        }

        return waterings.stream().map(WateringForOnePlant::from).collect(Collectors.toList());
    }

    public List<WateringForOnePlant> withWateringPeriodList(List<Watering> list) {
        List<WateringForOnePlant> waterings = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            // 마지막 요소이자 가장 옛날 물주기
            // => 전 요소가 없으므로 며칠만에 물 줬는지 계산 X
            if (i == list.size() - 1) {
                waterings.add(WateringForOnePlant.from(list.get(i)));
                break;
            }

            // 며칠만에 물줬는지 계산
            LocalDateTime afterWateringDate = list.get(i).getWateringDate().atStartOfDay();
            LocalDateTime prevWateringDate = list.get(i + 1).getWateringDate().atStartOfDay();

            int wateringPeriod = (int) Duration.between(prevWateringDate, afterWateringDate).toDays();

            waterings.add(WateringForOnePlant.withWateringPeriodFrom(list.get(i), wateringPeriod));
        }

        return waterings;
    }

    @Override
    public PlantWateringResponse modify(WateringRequest wateringRequest, Pageable pageable, Long gardenerId) {
        AfterWatering afterWatering = wateringDao.update(wateringRequest, gardenerId);

        Plant plant = afterWatering.getPlant();
        WateringMessage wateringMsg = afterWatering.getWateringMessage();
        List<WateringForOnePlant> waterings = getAll(plant.getPlantId(), pageable);

        return PlantWateringResponse.from(PlantResponse.from(plant), wateringMsg, waterings);
    }

    @Override
    public void delete(Long wateringId, Long plantId, Long gardenerId) {
        wateringDao.deleteById(wateringId, plantId, gardenerId);
    }

    @Override
    public void deleteAll(Long plantId) {
        wateringDao.deleteByPlantId(plantId);
    }
}
