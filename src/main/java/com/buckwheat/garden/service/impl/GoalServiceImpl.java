package com.buckwheat.garden.service.impl;

import com.buckwheat.garden.dao.GoalDao;
import com.buckwheat.garden.data.dto.GoalDto;
import com.buckwheat.garden.data.entity.Goal;
import com.buckwheat.garden.data.entity.Member;
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
    public List<GoalDto.Response> getGoalList(int memberNo) {
        List<GoalDto.Response> goalList = new ArrayList<>();

        for(Goal goal : goalDao.getGoalListBy(memberNo)){
            goalList.add(GoalDto.Response.from(goal));
        }

        return goalList;
    }

    @Override
    public GoalDto.Response addGoal(GoalDto.Request goalDto, Member member) {
        Goal goal = goalDao.save(goalDto, member);
        return GoalDto.Response.from(goal, goal.getPlant());
    }

    @Override
    public GoalDto.Response modifyGoal(GoalDto.Request goalDto) {
        Goal goal = goalDao.update(goalDto);
        return GoalDto.Response.from(goal, goal.getPlant());
    }

    @Override
    public GoalDto.Response completeGoal(int goalNo) {
        return GoalDto.Response.from(goalDao.complete(goalNo));
    }

    @Override
    public void deleteGoal(int goalNo) {
        goalDao.delete(goalNo);
    }
}
