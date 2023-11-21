package com.buckwheat.garden.domain.watering.service;

import com.buckwheat.garden.domain.plant.dto.garden.GardenWateringResponse;
import com.buckwheat.garden.domain.watering.dto.WateringMessage;
import com.buckwheat.garden.domain.watering.dto.WateringRequest;

public interface GardenWateringService {
    GardenWateringResponse add(Long gardenerId, WateringRequest wateringRequest);

    WateringMessage notDry(Long plantId);

    int postpone(Long plantId);
}
