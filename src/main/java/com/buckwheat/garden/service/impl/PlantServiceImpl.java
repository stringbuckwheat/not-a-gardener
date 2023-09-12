package com.buckwheat.garden.service.impl;

import com.buckwheat.garden.dao.PlantDao;
import com.buckwheat.garden.data.dto.GardenDto;
import com.buckwheat.garden.data.dto.PlaceDto;
import com.buckwheat.garden.data.dto.PlantDto;
import com.buckwheat.garden.data.dto.WateringDto;
import com.buckwheat.garden.data.entity.Plant;
import com.buckwheat.garden.data.projection.Calculate;
import com.buckwheat.garden.service.PlantService;
import com.buckwheat.garden.util.WateringUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class PlantServiceImpl implements PlantService {
    private final PlantDao plantDao;
    private final GardenResponseProvider gardenResponseProvider;
    private final WateringUtil wateringUtil;

    /**
     * 전체 식물 리스트
     * @param gardenerId
     * @return
     */
    @Override
    public List<PlantDto.Response> getAll(Long gardenerId) {
        return plantDao.getPlantsByGardenerId(gardenerId).stream()
                .map(PlantDto.Response::from)
                .collect(Collectors.toList());
    }

    /**
     * 하나의 식물 정보 반환
     *
     * @param plantId
     * @param gardenerId
     * @return
     */
    @Override
    public PlantDto.Detail getDetail(Long plantId, Long gardenerId) {
        Plant plant = plantDao.getPlantWithPlantIdAndGardenerId(plantId, gardenerId);

        List<WateringDto.ForOnePlant> waterings = wateringUtil.withWateringPeriodList(plant.getWaterings());

        return new PlantDto.Detail(PlantDto.Response.from(plant), waterings);
    }

    @Override
    public GardenDto.Response add(Long gardenerId, PlantDto.Request plantRequest) {
        Plant plant = plantDao.save(gardenerId, plantRequest);
        return gardenResponseProvider.getGardenResponse(Calculate.from(plant, gardenerId));
    }

    @Override
    @Transactional
    public GardenDto.Response modify(Long gardenerId, PlantDto.Request plantRequest) {
        Plant plant = plantDao.update(plantRequest, gardenerId);
        return gardenResponseProvider.getGardenResponse(Calculate.from(plant, gardenerId));
    }

    @Override
    public PlaceDto.Basic modifyPlace(PlaceDto.ModifyPlace modifyPlace, Long gardenerId) {
        return PlaceDto.Basic.from(plantDao.updatePlantPlace(modifyPlace, gardenerId));
    }

    @Override
    public void delete(Long plantId, Long gardenerId) {
        plantDao.deleteBy(plantId, gardenerId);
    }
}
