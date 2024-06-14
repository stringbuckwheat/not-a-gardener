package xyz.notagardener.status.garden;

import xyz.notagardener.plant.garden.dto.AttentionRequiredPlant;
import xyz.notagardener.status.plant.dto.PlantStatusRequest;

public interface GardenStatusService {
    AttentionRequiredPlant add(PlantStatusRequest request, Long gardenerId);
}
