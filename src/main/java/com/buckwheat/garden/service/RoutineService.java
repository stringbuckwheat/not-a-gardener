package com.buckwheat.garden.service;

import com.buckwheat.garden.data.dto.RoutineDto;

public interface RoutineService {
    // 오늘 해야할 일과 전체루틴 리스트
    RoutineDto.Main getRoutinesByMemberId(Long memberId);

    // 루틴 추가
    RoutineDto.Response add(Long memberId, RoutineDto.Request routineRequest);

    // 루틴 수정
    RoutineDto.Response modify(RoutineDto.Request routineRequest);

    // 루틴 완료
    RoutineDto.Response complete(RoutineDto.Complete routineComplete);

    void delete(Long id);
}
