package com.buckwheat.garden.repository;

import com.buckwheat.garden.data.entity.Watering;

import java.time.LocalDate;
import java.util.List;

public interface WateringRepositoryCustom {
    List<Watering> findAllWateringListByGardenerId(Long gardenerId, LocalDate startDate, LocalDate endDate);
}
