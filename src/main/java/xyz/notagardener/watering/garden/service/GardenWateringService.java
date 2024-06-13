package xyz.notagardener.watering.garden.service;

import xyz.notagardener.watering.garden.dto.GardenWateringResponse;
import xyz.notagardener.watering.watering.dto.WateringMessage;
import xyz.notagardener.watering.watering.dto.WateringRequest;

public interface GardenWateringService {
    GardenWateringResponse add(Long gardenerId, WateringRequest wateringRequest);

    WateringMessage notDry(Long plantId, Long gardenerId);

    String postpone(Long plantId, Long gardenerId);
}
