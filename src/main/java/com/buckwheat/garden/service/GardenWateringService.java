package com.buckwheat.garden.service;

import com.buckwheat.garden.data.dto.garden.GardenWateringResponse;
import com.buckwheat.garden.data.dto.watering.WateringMessage;
import com.buckwheat.garden.data.dto.watering.WateringRequest;

public interface GardenWateringService {
    GardenWateringResponse add(Long gardenerId, WateringRequest wateringRequest);

    WateringMessage notDry(Long plantId);

    int postpone(Long plantId);
}
