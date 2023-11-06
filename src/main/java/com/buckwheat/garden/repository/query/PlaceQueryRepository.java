package com.buckwheat.garden.repository.query;

import com.buckwheat.garden.data.entity.Place;
import com.buckwheat.garden.repository.query.querydsl.PlaceRepositoryCustom;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

public interface PlaceQueryRepository extends Repository<Place, Long>, PlaceRepositoryCustom {
    List<Place> findByGardener_GardenerIdOrderByCreateDate(Long gardenerId);
    Optional<Place> findByPlaceIdAndGardener_GardenerId(Long placeId, Long gardenerId);
}
