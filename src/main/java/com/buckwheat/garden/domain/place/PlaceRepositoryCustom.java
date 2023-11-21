package com.buckwheat.garden.domain.place;

import com.buckwheat.garden.domain.plant.Plant;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PlaceRepositoryCustom {
    Long countPlantsByPlaceId(Long placeId);

    List<Plant> findPlantsByPlaceIdWithPage(Long placeId, Pageable pageable);
}
