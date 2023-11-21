package com.buckwheat.garden.domain.watering.repository;

import com.buckwheat.garden.domain.watering.Watering;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface WateringRepositoryCustom {
    List<Watering> findAllWateringListByGardenerId(Long gardenerId, LocalDate startDate, LocalDate endDate);

    LocalDate findLatestWateringDate(Long plantId);

    Boolean existByWateringDateAndPlantId(LocalDate wateringDate, Long plantId);

    List<Watering> findLatestFourWateringDate(Long plantId);

    List<Watering> findWateringsByPlantIdWithPage(Long plantId, Pageable pageable);
}
