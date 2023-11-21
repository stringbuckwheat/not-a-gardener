package com.buckwheat.garden.watering;

import com.buckwheat.garden.plant.garden.GardenWateringResponse;
import com.buckwheat.garden.watering.dto.WateringMessage;
import com.buckwheat.garden.watering.dto.WateringRequest;

public interface GardenWateringService {
    GardenWateringResponse add(Long gardenerId, WateringRequest wateringRequest);

    WateringMessage notDry(Long plantId);

    int postpone(Long plantId);
}
