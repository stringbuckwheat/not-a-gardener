package com.buckwheat.garden.service.impl;

import com.buckwheat.garden.dao.GoalDao;
import com.buckwheat.garden.data.dto.GoalDto;
import com.buckwheat.garden.data.entity.Goal;
import com.buckwheat.garden.service.GoalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class GoalServiceImpl implements GoalService {
    private final GoalDao goalDao;

    @Override
    public List<GoalDto.Response> getGoalsByGardenerId(Long gardenerId) {
        List<GoalDto.Response> goalList = new ArrayList<>();

        for(Goal goal : goalDao.getGoalListBy(gardenerId)){
            goalList.add(GoalDto.Response.from(goal));
        }

        return goalList;
    }

    @Override
    public GoalDto.Response add(Long gardenerId, GoalDto.Request goalRequest) {
        Goal goal = goalDao.save(gardenerId, goalRequest);
        return GoalDto.Response.from(goal, goal.getPlant());
    }

    @Override
    public GoalDto.Response modify(GoalDto.Request goalRequest) {
        Goal goal = goalDao.update(goalRequest);
        return GoalDto.Response.from(goal, goal.getPlant());
    }

    @Override
    public GoalDto.Response complete(Long id) {
        return GoalDto.Response.from(goalDao.complete(id));
    }

    @Override
    public void delete(Long id) {
        goalDao.deleteBy(id);
    }
}
