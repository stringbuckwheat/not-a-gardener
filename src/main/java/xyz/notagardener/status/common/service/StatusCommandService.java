package xyz.notagardener.status.common.service;

import xyz.notagardener.status.common.model.Status;
import xyz.notagardener.status.common.model.StatusEntities;
import xyz.notagardener.status.plant.dto.PlantStatusRequest;

public interface StatusCommandService {
    StatusEntities add(PlantStatusRequest request, Long gardenerId);
    StatusEntities update(PlantStatusRequest request, Long gardenerId);
    Status delete(Long statusId, Long gardenerId);
}
