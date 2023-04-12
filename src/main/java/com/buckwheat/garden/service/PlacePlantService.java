package com.buckwheat.garden.service;

import com.buckwheat.garden.data.dto.PlantDto;

public interface PlacePlantService {
    PlantDto.PlantInPlace addPlantInPlace(PlantDto.PlantRequest plantRequestDto, int memberNo);
}
