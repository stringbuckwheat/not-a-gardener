package com.buckwheat.garden.repository.command;

import com.buckwheat.garden.data.dto.routine.RoutineComplete;
import com.buckwheat.garden.data.dto.routine.RoutineRequest;
import com.buckwheat.garden.data.entity.Routine;

public interface RoutineCommandRepository {
    Routine save(Long gardenerId, RoutineRequest routineRequest);

    Routine update(RoutineRequest routineRequest);

    Routine complete(RoutineComplete routineComplete);

    void deleteBy(Long id);
}
