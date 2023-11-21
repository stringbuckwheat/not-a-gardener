package com.buckwheat.garden.routine;

import com.buckwheat.garden.routine.routine.RoutineComplete;
import com.buckwheat.garden.routine.routine.RoutineMain;
import com.buckwheat.garden.routine.routine.RoutineRequest;
import com.buckwheat.garden.routine.routine.RoutineResponse;

public interface RoutineService {
    RoutineMain getAll(Long gardenerId);

    RoutineResponse add(Long gardenerId, RoutineRequest routineRequest);

    RoutineResponse update(RoutineRequest routineRequest);

    RoutineResponse complete(RoutineComplete routineComplete);

    void delete(Long id);
}
