package com.buckwheat.garden.repository.command;

import com.buckwheat.garden.data.dto.goal.GoalDto;
import com.buckwheat.garden.data.entity.Goal;

public interface GoalCommandRepository {
    Goal save(Long gardenerId, GoalDto goalRequest);

    Goal update(GoalDto goalRequest);

    Goal complete(Long goalId);

    void deleteBy(Long id);
}
