package com.buckwheat.garden.service;

import com.buckwheat.garden.data.dto.routine.RoutineComplete;
import com.buckwheat.garden.data.dto.routine.RoutineMain;
import com.buckwheat.garden.data.dto.routine.RoutineRequest;
import com.buckwheat.garden.data.dto.routine.RoutineResponse;

public interface RoutineService {
    RoutineMain getAll(Long gardenerId);

    RoutineResponse add(Long gardenerId, RoutineRequest routineRequest);

    RoutineResponse update(RoutineRequest routineRequest);

    RoutineResponse complete(RoutineComplete routineComplete);

    void delete(Long id);
}
