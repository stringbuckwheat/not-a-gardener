package com.buckwheat.garden.repository;

import com.buckwheat.garden.data.entity.Routine;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoutineRepository extends JpaRepository<Routine, Long> {
    @EntityGraph(attributePaths = {"plant"}, type = EntityGraph.EntityGraphType.FETCH)
    List<Routine> findByGardener_GardenerId(Long gardenerId);

    @EntityGraph(attributePaths = {"plant"}, type = EntityGraph.EntityGraphType.FETCH)
    Optional<Routine> findByRoutineId(Long routineId);
}
