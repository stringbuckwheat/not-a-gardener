package xyz.notagardener.place;

import org.springframework.data.domain.Pageable;
import xyz.notagardener.plant.Plant;

import java.util.List;

public interface PlaceRepositoryCustom {
    Long countPlantsByPlaceId(Long placeId);

    List<Plant> findPlantsByPlaceIdWithPage(Long placeId, Pageable pageable);
}
