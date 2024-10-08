package xyz.notagardener.plant.plant.repository;

import xyz.notagardener.plant.model.Plant;
import xyz.notagardener.plant.garden.dto.PlantResponse;
import xyz.notagardener.plant.garden.dto.WaitingForWatering;
import xyz.notagardener.plant.plant.dto.PlantBasic;

import java.util.List;
import java.util.Optional;

public interface PlantRepositoryCustom {
    Boolean existByGardenerId(Long gardenerId);
    List<WaitingForWatering> findWaitingForWateringList(Long gardenerId);
    List<PlantResponse> findAllPlantsWithLatestWateringDate(Long gardenerId);
    Optional<PlantResponse> findPlantWithLatestWateringDate(Long plantId, Long gardnerId);
    List<PlantBasic> findAttentionNotRequiredPlants(Long gardenerId);
    List<Plant> findAttentionRequiredPlants(Long gardenerId);
}
