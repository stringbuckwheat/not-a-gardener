package xyz.notagardener.status.service;

import xyz.notagardener.status.dto.PlantStatusRequest;
import xyz.notagardener.status.dto.PlantStatusResponse;

public interface PlantStatusService {
    // CREATE
    PlantStatusResponse add(PlantStatusRequest request, Long gardenerId);

    // UPDATE
    PlantStatusResponse update(PlantStatusRequest request, Long gardenerId);

    // DELETE
    void delete(Long plantStatusId, Long gardenerId);
}
