package com.buckwheat.garden.service.impl;

import com.buckwheat.garden.dao.GoalDao;
import com.buckwheat.garden.data.dto.goal.GoalDto;
import com.buckwheat.garden.data.entity.Goal;
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
    private final GoalDao goalDao;

    @Override
    public List<GoalDto> getAll(Long gardenerId) {
        return goalDao.getGoalListBy(gardenerId).stream()
                .map(GoalDto::from)
                .collect(Collectors.toList());
    }

    @Override
    public GoalDto add(Long gardenerId, GoalDto goalRequest) {
        Goal goal = goalDao.save(gardenerId, goalRequest);
        return GoalDto.from(goal);
    }

    @Override
    public GoalDto modify(GoalDto goalRequest) {
        Goal goal = goalDao.update(goalRequest);
        return GoalDto.from(goal);
    }

    @Override
    @Transactional
    public GoalDto complete(Long id) {
        return GoalDto.from(goalDao.complete(id));
    }

    @Override
    public void delete(Long id) {
        goalDao.deleteBy(id);
    }
}
