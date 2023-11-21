package com.buckwheat.garden.plant;

import com.buckwheat.garden.data.entity.Plant;
import com.buckwheat.garden.plant.garden.WaitingForWatering;

import java.time.LocalDate;
import java.util.List;

public interface PlantRepositoryCustom {
    Boolean existByGardenerId(Long gardenerId);

    List<WaitingForWatering> findWaitingForWateringList(Long gardenerId);

    List<Plant> findAllPlants(Long gardenerId);

    Long countWateringByPlantId(Long plantId);

    LocalDate findLatestWateringDateByPlantId(Long plantId);
}
