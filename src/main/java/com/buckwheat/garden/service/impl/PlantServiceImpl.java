package com.buckwheat.garden.service.impl;

import com.buckwheat.garden.dao.PlantDao;
import com.buckwheat.garden.data.dto.GardenDto;
import com.buckwheat.garden.data.dto.PlaceDto;
import com.buckwheat.garden.data.dto.PlantDto;
import com.buckwheat.garden.data.entity.Plant;
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
     * 하나의 장소 정보 반환
     *
     * @param id
     * @return
     */
    @Override
    public PlantDto.Response getPlantDetail(Long id) {
        return PlantDto.Response.from(plantDao.getPlantWithPlaceAndWatering(id));
    }

    @Override
    public List<PlantDto.Response> getPlantsByGardenerId(Long gardenerId) {
        List<PlantDto.Response> plants = new ArrayList<>();

        // @EntityGraph 메소드
        for (Plant p : plantDao.getPlantsByGardenerId(gardenerId)) {
            plants.add(PlantDto.Response.from(p));
        }

        return plants;
    }

    @Override
    public GardenDto.Response add(Long gardenerId, PlantDto.Request plantRequest) {
        Plant plant = plantDao.save(gardenerId, plantRequest);
        return gardenResponseProvider.getGardenResponse(plant, gardenerId);
    }

    @Override
    @Transactional
    public GardenDto.Response modify(Long gardenerId, PlantDto.Request plantRequest) {
        Plant plant = plantDao.update(plantRequest);
        return gardenResponseProvider.getGardenResponse(plant, gardenerId);
    }

    @Override
    public PlaceDto.Response modifyPlantPlace(PlaceDto.ModifyPlantPlace modifyPlantPlace) {
        return PlaceDto.Response.from(plantDao.updatePlantPlace(modifyPlantPlace));
    }

    @Override
    public void delete(Long id) {
        plantDao.deleteBy(id);
    }
}
