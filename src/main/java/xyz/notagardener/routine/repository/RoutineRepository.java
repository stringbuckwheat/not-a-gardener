package xyz.notagardener.routine.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.Repository;
import xyz.notagardener.routine.model.Routine;

import java.util.List;
import java.util.Optional;

public interface RoutineRepository extends Repository<Routine, Long> {
    @EntityGraph(attributePaths = {"plant"}, type = EntityGraph.EntityGraphType.FETCH)
    Optional<Routine> findByRoutineId(Long routineId);

    @EntityGraph(attributePaths = {"plant"}, type = EntityGraph.EntityGraphType.FETCH)
    List<Routine> findByGardener_GardenerId(Long gardenerId);

    Routine save(Routine routine);
    void delete(Routine routine);
}
