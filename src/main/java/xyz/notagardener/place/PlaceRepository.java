package xyz.notagardener.domain.place;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

public interface PlaceRepository extends Repository<Place, Long>, PlaceRepositoryCustom {
    @EntityGraph(attributePaths = {"plants"}, type = EntityGraph.EntityGraphType.FETCH)
    Optional<Place> findByPlaceIdAndGardener_GardenerId(Long placeId, Long gardenerId);
    List<Place> findByGardener_GardenerIdOrderByCreateDate(Long gardenerId);

    Place save(Place place);

    void deleteById(Long placeId);
}
