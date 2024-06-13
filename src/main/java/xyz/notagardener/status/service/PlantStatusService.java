package xyz.notagardener.status.service;

import xyz.notagardener.status.dto.AddStatusResponse;
import xyz.notagardener.status.dto.PlantStatusRequest;
import xyz.notagardener.status.dto.PlantStatusResponse;

public interface PlantStatusService {
    AddStatusResponse add(PlantStatusRequest request, Long gardenerId);

    PlantStatusResponse update(PlantStatusRequest request, Long gardenerId);

    PlantStatusResponse delete(Long plantId, Long statusId, Long gardenerId);
}
