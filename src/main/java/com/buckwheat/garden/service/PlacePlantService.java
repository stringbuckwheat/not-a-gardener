package com.buckwheat.garden.service;

import com.buckwheat.garden.data.dto.PlantDto;

public interface PlacePlantService {
    PlantDto.PlantInPlace addPlantInPlace(Long memberId, PlantDto.Request plantRequest);
}
