package com.buckwheat.garden.repository.command;

import com.buckwheat.garden.data.dto.watering.AfterWatering;
import com.buckwheat.garden.data.dto.watering.WateringMessage;
import com.buckwheat.garden.data.dto.watering.WateringRequest;

public interface WateringCommandRepository {
    AfterWatering add(WateringRequest wateringRequest);
    WateringMessage updateWateringPeriod(Long plantId);
    AfterWatering update(WateringRequest wateringRequest, Long gardenerId);
    WateringMessage deleteById(Long wateringId, Long plantId, Long gardenerId);
    void deleteByPlantId(Long plantId);
}
