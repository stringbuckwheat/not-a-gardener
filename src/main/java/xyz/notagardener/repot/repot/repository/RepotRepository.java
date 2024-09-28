package xyz.notagardener.repot.repot.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.Repository;
import xyz.notagardener.repot.model.Repot;

import java.util.Optional;

public interface RepotRepository extends Repository<Repot, Long>, RepotRepositoryCustom {
    @EntityGraph(attributePaths = {"plant", "plant.gardener"}, type = EntityGraph.EntityGraphType.FETCH)
    Optional<Repot> findByRepotId(Long repotId);

    Optional<Repot> findTopByPlant_PlantIdOrderByRepotDateDesc(Long plantId);

    Repot save(Repot repot);

    void delete(Repot repot);
}
