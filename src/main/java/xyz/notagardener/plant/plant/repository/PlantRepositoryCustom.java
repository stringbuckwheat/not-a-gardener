package xyz.notagardener.plant.plant.repository;

import xyz.notagardener.plant.Plant;
import xyz.notagardener.plant.garden.dto.PlantResponse;
import xyz.notagardener.plant.garden.dto.WaitingForWatering;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PlantRepositoryCustom {
    Boolean existByGardenerId(Long gardenerId);

    List<WaitingForWatering> findWaitingForWateringList(Long gardenerId);

    List<Plant> findAllPlants(Long gardenerId);
    List<PlantResponse> findAllPlantsWithLatestWateringDate(Long gardenerId);
    Optional<PlantResponse> findPlantWithLatestWateringDate(Long plantId, Long gardnerId);

    Long countWateringByPlantId(Long plantId);

    LocalDate findLatestWateringDateByPlantId(Long plantId);
}
