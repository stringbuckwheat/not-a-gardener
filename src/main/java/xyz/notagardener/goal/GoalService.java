package xyz.notagardener.goal;

import java.util.List;

public interface GoalService {
    List<GoalDto> getAll(Long gardenerId);

    GoalDto add(Long gardenerId, GoalDto goalRequest);

    GoalDto update(Long gardenerId, GoalDto goalRequest);

    GoalDto complete(Long goalId, Long gardenerId);

    void delete(Long goalId, Long gardenerId);
}
