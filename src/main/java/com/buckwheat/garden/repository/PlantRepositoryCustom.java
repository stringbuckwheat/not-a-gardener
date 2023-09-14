package com.buckwheat.garden.repository;

import com.buckwheat.garden.data.entity.Plant;

import java.util.List;

public interface PlantRepositoryCustom {
    List<Plant> findWaitingForWateringList(Long gardenerId);
}
