package com.buckwheat.garden.dao;

import com.buckwheat.garden.data.dto.RoutineDto;
import com.buckwheat.garden.data.entity.Routine;

import java.util.List;

public interface RoutineDao {
    List<Routine> getRoutinesByMemberId(Long memberId);
    Routine save(Long memberId, RoutineDto.Request routineRequest);
    Routine update(RoutineDto.Request routineRequest);
    Routine complete(RoutineDto.Complete routineComplete);
    void deleteBy(Long id);
}
