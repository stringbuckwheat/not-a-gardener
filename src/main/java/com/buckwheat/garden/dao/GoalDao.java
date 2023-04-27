package com.buckwheat.garden.dao;

import com.buckwheat.garden.data.dto.GoalDto;
import com.buckwheat.garden.data.entity.Goal;

import java.util.List;

public interface GoalDao {
    List<Goal> getGoalListBy(Long memberId);
    Goal save(Long memberId, GoalDto.Request goalRequest);
    Goal update(GoalDto.Request goalRequest);
    Goal complete(Long goalId);
    void deleteBy(Long id);
}
