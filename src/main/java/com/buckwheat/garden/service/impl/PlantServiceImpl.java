package com.buckwheat.garden.service.impl;

import com.buckwheat.garden.dao.PlantDao;
import com.buckwheat.garden.data.dto.*;
import com.buckwheat.garden.data.entity.Plant;
import com.buckwheat.garden.data.entity.Watering;
import com.buckwheat.garden.service.PlantService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PlantServiceImpl implements PlantService {
    private final PlantDao plantDao;
    private final GardenResponseProvider gardenResponseProvider;

    /**
     * 전체 식물 리스트
     * @param gardenerId
     * @return
     */
    @Override
    public List<PlantDto.Response> getPlantsByGardenerId(Long gardenerId) {
        List<PlantDto.Response> plants = new ArrayList<>();

        // @EntityGraph 메소드
        for (Plant p : plantDao.getPlantsByGardenerId(gardenerId)) {
            plants.add(PlantDto.Response.from(p));
        }

        return plants;
    }

    /**
     * 하나의 식물 정보 반환
     *
     * @param plantId
     * @param gardenerId
     * @return
     */
    @Override
    public PlantDto.Detail getPlantDetail(Long plantId, Long gardenerId) {
        Plant plant = plantDao.getPlantWithPlantIdAndGardenerId(plantId, gardenerId);

        List<WateringDto.ForOnePlant> waterings = new ArrayList<>();

        for(Watering watering : plant.getWaterings()){
            waterings.add(WateringDto.ForOnePlant.from(watering));
        }

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
    public PlaceDto.Response modifyPlantPlace(PlaceDto.ModifyPlantPlace modifyPlantPlace, Long gardenerId) {
        return PlaceDto.Response.from(plantDao.updatePlantPlace(modifyPlantPlace, gardenerId));
    }

    @Override
    public void delete(Long plantId, Long gardenerId) {
        plantDao.deleteBy(plantId, gardenerId);
    }
}
