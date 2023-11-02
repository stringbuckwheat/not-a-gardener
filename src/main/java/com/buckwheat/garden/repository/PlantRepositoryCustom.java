package com.buckwheat.garden.repository;

import com.buckwheat.garden.data.dto.garden.WaitingForWatering;
import com.buckwheat.garden.data.entity.Plant;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PlantRepositoryCustom {
    /**
     * javadoc class
     * @param gardenerId
     * @return
     */
    Boolean existByGardenerId(Long gardenerId);
    List<WaitingForWatering> findWaitingForWateringList(Long gardenerId);
    List<Plant> findPlantsByPlaceIdWithPage(Long placeId, Pageable pageable);
}
