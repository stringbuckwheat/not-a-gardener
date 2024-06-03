package xyz.notagardener.status.repository;

import xyz.notagardener.status.dto.SimplePlantStatus;

import java.util.List;

public interface PlantStatusRepositoryCustom {
    List<SimplePlantStatus> findByPlantId(Long plantId, Long gardenerId, List<String> statusList);
}
