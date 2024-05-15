package xyz.notagardener.place;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

public interface PlaceRepository extends Repository<Place, Long>, PlaceRepositoryCustom {
    @EntityGraph(attributePaths = {"plants", "gardener"}, type = EntityGraph.EntityGraphType.FETCH)
    Optional<Place> findByPlaceId(Long placeId);
    @EntityGraph(attributePaths = {"plants"}, type = EntityGraph.EntityGraphType.FETCH)
    Optional<Place> findByPlaceIdAndGardener_GardenerId(Long placeId, Long gardenerId);
    List<Place> findByGardener_GardenerIdOrderByCreateDate(Long gardenerId);

    Place save(Place place);

    void deleteById(Long placeId);
    void delete(Place place);
}
