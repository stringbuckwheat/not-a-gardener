package com.buckwheat.garden.domain.routine;

import com.buckwheat.garden.domain.routine.dto.RoutineComplete;
import com.buckwheat.garden.domain.routine.dto.RoutineMain;
import com.buckwheat.garden.domain.routine.dto.RoutineRequest;
import com.buckwheat.garden.domain.routine.dto.RoutineResponse;

public interface RoutineService {
    RoutineMain getAll(Long gardenerId);

    RoutineResponse add(Long gardenerId, RoutineRequest routineRequest);

    RoutineResponse update(RoutineRequest routineRequest);

    RoutineResponse complete(RoutineComplete routineComplete);

    void delete(Long id);
}
