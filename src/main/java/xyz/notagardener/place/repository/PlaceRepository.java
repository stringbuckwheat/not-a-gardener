package xyz.notagardener.place.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.Repository;
import xyz.notagardener.place.model.Place;

import java.util.List;
import java.util.Optional;

public interface PlaceRepository extends Repository<Place, Long>, PlaceRepositoryCustom {
    @EntityGraph(attributePaths = {"plants", "gardener"}, type = EntityGraph.EntityGraphType.FETCH)
    Optional<Place> findByPlaceId(Long placeId);

    List<Place> findByGardener_GardenerIdOrderByCreateDate(Long gardenerId);

    Place save(Place place);

    void delete(Place place);
}
