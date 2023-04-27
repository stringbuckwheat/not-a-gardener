package com.buckwheat.garden.service;

import com.buckwheat.garden.data.dto.GardenDto;
import com.buckwheat.garden.data.dto.WateringDto;

public interface GardenWateringService {
    GardenDto.WateringResponse addWateringInGarden(Long memberId, WateringDto.Request wateringRequest);
    WateringDto.Message notDry(Long plantId);
    int postpone(Long plantId);
}
