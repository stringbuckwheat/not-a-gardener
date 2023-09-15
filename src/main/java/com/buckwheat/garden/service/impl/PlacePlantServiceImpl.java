package com.buckwheat.garden.service.impl;

import com.buckwheat.garden.dao.PlantDao;
import com.buckwheat.garden.data.dto.plant.PlantInPlace;
import com.buckwheat.garden.data.dto.plant.PlantRequest;
import com.buckwheat.garden.service.PlacePlantService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class PlacePlantServiceImpl implements PlacePlantService {
    private final PlantDao plantDao;

    @Override
    public PlantInPlace addPlantInPlace(Long gardenerId, PlantRequest plantRequest){
        return PlantInPlace.from(plantDao.save(gardenerId, plantRequest));
    }
}
