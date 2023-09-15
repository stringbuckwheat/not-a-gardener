package com.buckwheat.garden.service.impl;

import com.buckwheat.garden.dao.PlantDao;
import com.buckwheat.garden.data.dto.garden.GardenResponse;
import com.buckwheat.garden.data.dto.place.ModifyPlace;
import com.buckwheat.garden.data.dto.place.PlaceDto;
import com.buckwheat.garden.data.dto.plant.PlantDetail;
import com.buckwheat.garden.data.dto.plant.PlantRequest;
import com.buckwheat.garden.data.dto.plant.PlantResponse;
import com.buckwheat.garden.data.dto.watering.WateringForOnePlant;
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
    public List<PlantResponse> getAll(Long gardenerId) {
        return plantDao.getPlantsByGardenerId(gardenerId).stream()
                .map(PlantResponse::from)
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
    public PlantDetail getDetail(Long plantId, Long gardenerId) {
        Plant plant = plantDao.getPlantWithPlantIdAndGardenerId(plantId, gardenerId);

        List<WateringForOnePlant> waterings = wateringUtil.withWateringPeriodList(plant.getWaterings());

        return new PlantDetail(PlantResponse.from(plant), waterings);
    }

    @Override
    public GardenResponse add(Long gardenerId, PlantRequest plantRequest) {
        Plant plant = plantDao.save(gardenerId, plantRequest);
        return gardenResponseProvider.getGardenResponse(Calculate.from(plant, gardenerId));
    }

    @Override
    @Transactional
    public GardenResponse modify(Long gardenerId, PlantRequest plantRequest) {
        Plant plant = plantDao.update(plantRequest, gardenerId);
        return gardenResponseProvider.getGardenResponse(Calculate.from(plant, gardenerId));
    }

    @Override
    public PlaceDto modifyPlace(ModifyPlace modifyPlace, Long gardenerId) {
        return PlaceDto.from(plantDao.updatePlantPlace(modifyPlace, gardenerId));
    }

    @Override
    public void delete(Long plantId, Long gardenerId) {
        plantDao.deleteBy(plantId, gardenerId);
    }
}
