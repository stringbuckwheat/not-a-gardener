package xyz.notagardener.watering.watering.service;

import xyz.notagardener.plant.model.Plant;
import xyz.notagardener.watering.watering.dto.AfterWatering;
import xyz.notagardener.watering.watering.dto.WateringMessage;
import xyz.notagardener.watering.watering.dto.WateringRequest;

public interface WateringCommandService {
    AfterWatering add(WateringRequest wateringRequest, Long gardenerId);

    WateringMessage updateWateringPeriod(Plant plant);

    AfterWatering update(WateringRequest wateringRequest, Long gardenerId);

    WateringMessage deleteById(Long wateringId, Long plantId, Long gardenerId);

    void deleteByPlantId(Long plantId, Long gardenerId);
}
