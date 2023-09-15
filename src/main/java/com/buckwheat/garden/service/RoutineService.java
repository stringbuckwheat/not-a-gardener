package com.buckwheat.garden.service;

import com.buckwheat.garden.data.dto.routine.RoutineComplete;
import com.buckwheat.garden.data.dto.routine.RoutineMain;
import com.buckwheat.garden.data.dto.routine.RoutineRequest;
import com.buckwheat.garden.data.dto.routine.RoutineResponse;

public interface RoutineService {
    // 오늘 해야할 일과 전체루틴 리스트
    RoutineMain getAll(Long gardenerId);

    // 루틴 추가
    RoutineResponse add(Long gardenerId, RoutineRequest routineRequest);

    // 루틴 수정
    RoutineResponse modify(RoutineRequest routineRequest);

    // 루틴 완료
    RoutineResponse complete(RoutineComplete routineComplete);

    void delete(Long id);
}
