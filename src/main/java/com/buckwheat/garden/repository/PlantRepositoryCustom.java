package com.buckwheat.garden.repository;

import com.buckwheat.garden.data.entity.Plant;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PlantRepositoryCustom {
    List<Plant> findWaitingForWateringList(Long gardenerId);
    List<Plant> findPlantsByPlaceIdWithPage(Long placeId, Pageable pageable);
}
