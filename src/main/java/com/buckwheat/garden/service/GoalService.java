package com.buckwheat.garden.service;

import com.buckwheat.garden.data.dto.GoalDto;
import com.buckwheat.garden.data.entity.Member;

import java.util.List;

public interface GoalService {
    List<GoalDto.Response> getGoalList(int memberNo);

    GoalDto.Response addGoal(GoalDto.Request goalDto, Member member);

    GoalDto.Response modifyGoal(GoalDto.Request goalDto);

    GoalDto.Response completeGoal(int goalNo);

    void deleteGoal(int goalNo);
}
