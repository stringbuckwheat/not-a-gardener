package com.buckwheat.garden.service;

import com.buckwheat.garden.data.dto.GardenDto;
import com.buckwheat.garden.data.dto.WateringDto;
import com.buckwheat.garden.data.entity.Member;

public interface GardenWateringService {
    GardenDto.WateringResponse addWateringInGarden(Member member, WateringDto.Request wateringRequest);
    WateringDto.Message notDry(int plantNo);
    int postpone(int plantNo);
}
