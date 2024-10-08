package xyz.notagardener.place.repository;

import org.springframework.data.domain.Pageable;
import xyz.notagardener.plant.model.Plant;

import java.util.List;

public interface PlaceRepositoryCustom {
    Long countPlantsByPlaceId(Long placeId);

    List<Plant> findPlantsByPlaceIdWithPage(Long placeId, Pageable pageable);
}
