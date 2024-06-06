package xyz.notagardener.status.service;

import xyz.notagardener.status.dto.PlantStatusRequest;
import xyz.notagardener.status.dto.PlantStatusResponse;

import java.util.List;

public interface PlantStatusService {
    PlantStatusResponse add(PlantStatusRequest request, Long gardenerId);

    List<PlantStatusResponse> getAll(Long plantId, Long gardenerId);

    PlantStatusResponse update(PlantStatusRequest request, Long gardenerId);

    List<PlantStatusResponse> delete(Long plantId, Long plantStatusId, Long gardenerId);
}
