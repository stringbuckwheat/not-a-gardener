package com.buckwheat.garden.dao;

import com.buckwheat.garden.data.dto.GoalDto;
import com.buckwheat.garden.data.entity.Goal;

import java.util.List;

public interface GoalDao {
    List<Goal> getGoalListBy(Long gardenerId);
    Goal save(Long gardenerId, GoalDto.Basic goalRequest);
    Goal update(GoalDto.Basic goalRequest);
    Goal complete(Long goalId);
    void deleteBy(Long id);
}
