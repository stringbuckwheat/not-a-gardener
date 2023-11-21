package com.buckwheat.garden.watering;

import com.buckwheat.garden.data.entity.Plant;
import com.buckwheat.garden.watering.dto.AfterWatering;
import com.buckwheat.garden.watering.dto.WateringMessage;
import com.buckwheat.garden.watering.dto.WateringRequest;

public interface WateringCommandService {
    AfterWatering add(WateringRequest wateringRequest);

    WateringMessage updateWateringPeriod(Plant plant);

    AfterWatering update(WateringRequest wateringRequest, Long gardenerId);

    WateringMessage deleteById(Long wateringId, Long plantId, Long gardenerId);
    void deleteByPlantId(Long plantId);
}
