package xyz.notagardener.routine.service;

import xyz.notagardener.routine.dto.RoutineComplete;
import xyz.notagardener.routine.dto.RoutineMain;
import xyz.notagardener.routine.dto.RoutineRequest;
import xyz.notagardener.routine.dto.RoutineResponse;

public interface RoutineService {
    RoutineMain getAll(Long gardenerId);

    RoutineResponse add(RoutineRequest routineRequest, Long gardenerId);

    RoutineResponse update(RoutineRequest routineRequest, Long gardenerId);

    RoutineResponse complete(RoutineComplete routineComplete, Long gardenerId);

    void delete(Long routineId, Long gardenerId);
}
