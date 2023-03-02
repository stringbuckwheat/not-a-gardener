package com.buckwheat.garden.service;

import com.buckwheat.garden.data.dto.GoalDto;
import com.buckwheat.garden.data.entity.Member;

import java.util.List;

public interface GoalService {
    List<GoalDto> getGoalList(int memberNo);

    GoalDto addGoal(GoalDto goalDto, Member member);

    GoalDto modifyGoal(GoalDto goalDto);

    GoalDto completeGoal(int goalNo);

    void deleteGoal(int goalNo);
}
