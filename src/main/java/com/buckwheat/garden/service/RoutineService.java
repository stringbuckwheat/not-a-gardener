package com.buckwheat.garden.service;

import com.buckwheat.garden.data.dto.RoutineDto;
import com.buckwheat.garden.data.entity.Member;

public interface RoutineService {
    // 오늘 해야할 일과 전체루틴 리스트
    RoutineDto.Main getRoutineList(int memberNo);

    // 루틴 추가
    RoutineDto.Response addRoutine(Member member, RoutineDto.Request routineDto);

    // 루틴 수정
    RoutineDto.Response modifyRoutine(RoutineDto.Request routineDto);

    // 루틴 완료
    RoutineDto.Response complete(RoutineDto.Complete routineDto);

    // D
    void deleteRoutine(int routineNo);
}
