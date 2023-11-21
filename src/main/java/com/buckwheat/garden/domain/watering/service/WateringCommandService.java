package com.buckwheat.garden.domain.watering.service;

import com.buckwheat.garden.domain.plant.Plant;
import com.buckwheat.garden.domain.watering.dto.AfterWatering;
import com.buckwheat.garden.domain.watering.dto.WateringMessage;
import com.buckwheat.garden.domain.watering.dto.WateringRequest;

public interface WateringCommandService {
    AfterWatering add(WateringRequest wateringRequest);

    WateringMessage updateWateringPeriod(Plant plant);

    AfterWatering update(WateringRequest wateringRequest, Long gardenerId);

    WateringMessage deleteById(Long wateringId, Long plantId, Long gardenerId);
    void deleteByPlantId(Long plantId);
}
