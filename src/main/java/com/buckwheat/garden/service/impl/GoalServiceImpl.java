package com.buckwheat.garden.service.impl;

import com.buckwheat.garden.data.dto.goal.GoalDto;
import com.buckwheat.garden.data.entity.Goal;
import com.buckwheat.garden.repository.command.GoalCommandRepository;
import com.buckwheat.garden.repository.query.GoalQueryRepository;
import com.buckwheat.garden.service.GoalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class GoalServiceImpl implements GoalService {
    private final GoalCommandRepository goalCommandRepository;
    private final GoalQueryRepository goalQueryRepository;

    @Override
    @Transactional(readOnly = true)
    public List<GoalDto> getAll(Long gardenerId) {
        return goalQueryRepository.findByGardener_GardenerId(gardenerId).stream()
                .map(GoalDto::from)
                .collect(Collectors.toList());
    }

    @Override
    public GoalDto add(Long gardenerId, GoalDto goalRequest) {
        Goal goal = goalCommandRepository.save(gardenerId, goalRequest);
        return GoalDto.from(goal);
    }

    @Override
    public GoalDto update(GoalDto goalRequest) {
        Goal goal = goalCommandRepository.update(goalRequest);
        return GoalDto.from(goal);
    }

    @Override
    @Transactional
    public GoalDto complete(Long id) {
        return GoalDto.from(goalCommandRepository.complete(id));
    }

    @Override
    public void delete(Long id) {
        goalCommandRepository.deleteBy(id);
    }
}
