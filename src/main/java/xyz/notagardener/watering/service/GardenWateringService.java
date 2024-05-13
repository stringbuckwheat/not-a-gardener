package xyz.notagardener.domain.watering.service;

import xyz.notagardener.domain.plant.dto.garden.GardenWateringResponse;
import xyz.notagardener.domain.watering.dto.WateringMessage;
import xyz.notagardener.domain.watering.dto.WateringRequest;

public interface GardenWateringService {
    GardenWateringResponse add(Long gardenerId, WateringRequest wateringRequest);

    WateringMessage notDry(Long plantId);

    int postpone(Long plantId);
}
