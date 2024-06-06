package xyz.notagardener.status.repository;

import xyz.notagardener.status.dto.PlantStatusResponse;
import xyz.notagardener.status.dto.PlantStatusType;

import java.util.Optional;

public interface PlantStatusRepositoryCustom {
    Long deactivateStatusByPlantIdAndStatus(Long plantId, PlantStatusType status);
    Optional<PlantStatusResponse> findByPlantIdAndStatus(Long plantId, PlantStatusType status);
}
