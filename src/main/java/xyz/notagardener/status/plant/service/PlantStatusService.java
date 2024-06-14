package xyz.notagardener.status.plant.service;

import xyz.notagardener.status.plant.dto.AddStatusResponse;
import xyz.notagardener.status.plant.dto.PlantStatusRequest;
import xyz.notagardener.status.plant.dto.PlantStatusResponse;

public interface PlantStatusService {
    AddStatusResponse add(PlantStatusRequest request, Long gardenerId);

    PlantStatusResponse update(PlantStatusRequest request, Long gardenerId);

    PlantStatusResponse delete(Long plantId, Long statusId, Long gardenerId);
}
