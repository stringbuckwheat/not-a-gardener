package com.buckwheat.garden.repository;

import com.buckwheat.garden.data.entity.Place;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface PlaceRepository extends Repository<Place, Long> {
    @EntityGraph(attributePaths = {"plants"}, type= EntityGraph.EntityGraphType.FETCH)
    Optional<Place> findByPlaceIdAndGardener_GardenerId(Long placeId, Long gardenerId);

    Place save(Place place);
    void deleteById(Long placeId);
}
