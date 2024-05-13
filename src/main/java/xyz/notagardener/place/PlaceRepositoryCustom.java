package xyz.notagardener.domain.place;

import xyz.notagardener.domain.plant.Plant;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PlaceRepositoryCustom {
    Long countPlantsByPlaceId(Long placeId);

    List<Plant> findPlantsByPlaceIdWithPage(Long placeId, Pageable pageable);
}
