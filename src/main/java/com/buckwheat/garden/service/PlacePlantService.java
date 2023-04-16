package com.buckwheat.garden.service;

import com.buckwheat.garden.data.dto.PlantDto;

public interface PlacePlantService {
    PlantDto.PlantInPlace addPlantInPlace(PlantDto.Request plantRequest, int memberNo);
}
