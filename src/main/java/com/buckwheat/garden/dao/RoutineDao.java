package com.buckwheat.garden.dao;

import com.buckwheat.garden.data.dto.routine.RoutineComplete;
import com.buckwheat.garden.data.dto.routine.RoutineRequest;
import com.buckwheat.garden.data.entity.Routine;

import java.util.List;

public interface RoutineDao {
    List<Routine> getRoutinesByGardenerId(Long gardenerId);
    Routine save(Long gardenerId, RoutineRequest routineRequest);
    Routine update(RoutineRequest routineRequest);
    Routine complete(RoutineComplete routineComplete);
    void deleteBy(Long id);
}
