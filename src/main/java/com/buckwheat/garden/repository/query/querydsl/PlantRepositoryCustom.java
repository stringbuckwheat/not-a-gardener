package com.buckwheat.garden.repository.query.querydsl;

import com.buckwheat.garden.data.dto.garden.WaitingForWatering;
import com.buckwheat.garden.data.entity.Plant;

import java.time.LocalDate;
import java.util.List;

public interface PlantRepositoryCustom {
    Boolean existByGardenerId(Long gardenerId);
    List<WaitingForWatering> findWaitingForWateringList(Long gardenerId);
    List<Plant> findAllPlants(Long gardenerId);
    Long countWateringByPlantId(Long plantId);
    LocalDate findLatestWateringDateByPlantId(Long plantId);
}
