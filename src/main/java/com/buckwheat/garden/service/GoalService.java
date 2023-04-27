package com.buckwheat.garden.service;

import com.buckwheat.garden.data.dto.GoalDto;

import java.util.List;

public interface GoalService {
    List<GoalDto.Response> getGoalsByMemberId(Long memberId);

    GoalDto.Response add(Long memberId, GoalDto.Request goalRequest);

    GoalDto.Response modify(GoalDto.Request goalRequest);

    GoalDto.Response complete(Long id);

    void delete(Long id);
}
