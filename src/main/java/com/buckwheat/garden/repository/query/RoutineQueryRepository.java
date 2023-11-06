package com.buckwheat.garden.repository.query;


import com.buckwheat.garden.data.entity.Routine;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.Repository;

import java.util.List;

public interface RoutineQueryRepository extends Repository<Routine, Long> {
    @EntityGraph(attributePaths = {"plant"}, type = EntityGraph.EntityGraphType.FETCH)
    List<Routine> findByGardener_GardenerId(Long gardenerId);

}
