package com.buckwheat.garden.repository;

import com.buckwheat.garden.data.entity.Routine;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface RoutineRepository extends Repository<Routine, Long> {
    @EntityGraph(attributePaths = {"plant"}, type = EntityGraph.EntityGraphType.FETCH)
    Optional<Routine> findByRoutineId(Long routineId);

    Routine save(Routine routine);
    void deleteById(Long routineId);
}
