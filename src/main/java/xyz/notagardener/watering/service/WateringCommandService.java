package xyz.notagardener.domain.watering.service;

import xyz.notagardener.domain.plant.Plant;
import xyz.notagardener.domain.watering.dto.AfterWatering;
import xyz.notagardener.domain.watering.dto.WateringMessage;
import xyz.notagardener.domain.watering.dto.WateringRequest;

public interface WateringCommandService {
    AfterWatering add(WateringRequest wateringRequest);

    WateringMessage updateWateringPeriod(Plant plant);

    AfterWatering update(WateringRequest wateringRequest, Long gardenerId);

    WateringMessage deleteById(Long wateringId, Long plantId, Long gardenerId);
    void deleteByPlantId(Long plantId);
}
