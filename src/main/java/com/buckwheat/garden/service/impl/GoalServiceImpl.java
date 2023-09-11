package com.buckwheat.garden.service.impl;

import com.buckwheat.garden.dao.GoalDao;
import com.buckwheat.garden.data.dto.GoalDto;
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
    public List<GoalDto.Basic> getAll(Long gardenerId) {
        return goalDao.getGoalListBy(gardenerId).stream()
                .map(GoalDto.Basic::from)
                .collect(Collectors.toList());
    }

    @Override
    public GoalDto.Basic add(Long gardenerId, GoalDto.Basic goalRequest) {
        Goal goal = goalDao.save(gardenerId, goalRequest);
        return GoalDto.Basic.from(goal);
    }

    @Override
    public GoalDto.Basic modify(GoalDto.Basic goalRequest) {
        Goal goal = goalDao.update(goalRequest);
        return GoalDto.Basic.from(goal);
    }

    @Override
    @Transactional
    public GoalDto.Basic complete(Long id) {
        return GoalDto.Basic.from(goalDao.complete(id));
    }

    @Override
    public void delete(Long id) {
        goalDao.deleteBy(id);
    }
}
