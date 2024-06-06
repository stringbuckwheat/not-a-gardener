package xyz.notagardener.status.service;

import xyz.notagardener.status.dto.PlantStatusResponse;

import java.util.List;

public interface PlantStatusQueryService {
    List<PlantStatusResponse> getRecentStatusByPlantId(Long plantId, Long gardenerId);
}
