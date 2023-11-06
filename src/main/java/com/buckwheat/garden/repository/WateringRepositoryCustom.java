package com.buckwheat.garden.repository;

import com.buckwheat.garden.data.entity.Watering;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface WateringRepositoryCustom {
    List<Watering> findAllWateringListByGardenerId(Long gardenerId, LocalDate startDate, LocalDate endDate);
    List<Watering> findWateringsByChemicalIdWithPage(Long chemicalId, Pageable pageable);
    List<Watering> findWateringsByPlantIdWithPage(Long plantId, Pageable pageable);
    LocalDate findLatestWateringDate(Long plantId);
    Boolean existByWateringDateAndPlantId(LocalDate wateringDate, Long plantId);
}
