package com.buckwheat.garden.repository.query.querydsl;

import com.buckwheat.garden.data.entity.Plant;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PlaceRepositoryCustom {
    Long countPlantsByPlaceId(Long placeId);

    List<Plant> findPlantsByPlaceIdWithPage(Long placeId, Pageable pageable);
}
