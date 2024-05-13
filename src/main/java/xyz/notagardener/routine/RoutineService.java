package xyz.notagardener.domain.routine;

import xyz.notagardener.domain.routine.dto.RoutineComplete;
import xyz.notagardener.domain.routine.dto.RoutineMain;
import xyz.notagardener.domain.routine.dto.RoutineRequest;
import xyz.notagardener.domain.routine.dto.RoutineResponse;

public interface RoutineService {
    RoutineMain getAll(Long gardenerId);

    RoutineResponse add(Long gardenerId, RoutineRequest routineRequest);

    RoutineResponse update(RoutineRequest routineRequest);

    RoutineResponse complete(RoutineComplete routineComplete);

    void delete(Long id);
}
