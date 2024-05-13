package xyz.notagardener.domain.plant.repository;

import xyz.notagardener.domain.plant.Plant;
import xyz.notagardener.domain.plant.dto.garden.WaitingForWatering;

import java.time.LocalDate;
import java.util.List;

public interface PlantRepositoryCustom {
    Boolean existByGardenerId(Long gardenerId);

    List<WaitingForWatering> findWaitingForWateringList(Long gardenerId);

    List<Plant> findAllPlants(Long gardenerId);

    Long countWateringByPlantId(Long plantId);

    LocalDate findLatestWateringDateByPlantId(Long plantId);
}
