package com.buckwheat.garden.dao;

import com.buckwheat.garden.data.dto.RoutineDto;
import com.buckwheat.garden.data.entity.Member;
import com.buckwheat.garden.data.entity.Routine;

import java.util.List;

public interface RoutineDao {
    List<Routine> getRoutineListByMemberNo(int memberNo);
    Routine save(RoutineDto.Request routineDto, Member member);
    Routine update(RoutineDto.Request routineDto);
    Routine complete(RoutineDto.Complete routineDto);
    void delete(int routineNo);
}
