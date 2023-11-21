package com.buckwheat.garden.domain.plant.repository;

import com.buckwheat.garden.domain.plant.Plant;
import com.buckwheat.garden.domain.plant.dto.garden.WaitingForWatering;

import java.time.LocalDate;
import java.util.List;

public interface PlantRepositoryCustom {
    Boolean existByGardenerId(Long gardenerId);

    List<WaitingForWatering> findWaitingForWateringList(Long gardenerId);

    List<Plant> findAllPlants(Long gardenerId);

    Long countWateringByPlantId(Long plantId);

    LocalDate findLatestWateringDateByPlantId(Long plantId);
}
