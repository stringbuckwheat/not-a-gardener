package com.buckwheat.garden.service;

import com.buckwheat.garden.data.dto.GoalDto;

import java.util.List;

public interface GoalService {
    List<GoalDto.Basic> getAll(Long gardenerId);

    GoalDto.Basic add(Long gardenerId, GoalDto.Basic goalRequest);

    GoalDto.Basic modify(GoalDto.Basic goalRequest);

    GoalDto.Basic complete(Long id);

    void delete(Long id);
}
