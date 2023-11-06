package com.buckwheat.garden.service;

import com.buckwheat.garden.data.dto.goal.GoalDto;

import java.util.List;

public interface GoalService {
    List<GoalDto> getAll(Long gardenerId);

    GoalDto add(Long gardenerId, GoalDto goalRequest);

    GoalDto update(GoalDto goalRequest);

    GoalDto complete(Long id);

    void delete(Long id);
}
