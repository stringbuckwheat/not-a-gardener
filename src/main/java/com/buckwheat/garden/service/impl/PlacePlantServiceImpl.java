package com.buckwheat.garden.service.impl;

import com.buckwheat.garden.dao.PlantDao;
import com.buckwheat.garden.data.dto.PlantDto;
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
    public PlantDto.PlantInPlace addPlantInPlace(Long memberId, PlantDto.Request plantRequest){
        return PlantDto.PlantInPlace.from(plantDao.save(memberId, plantRequest));
    }
}
