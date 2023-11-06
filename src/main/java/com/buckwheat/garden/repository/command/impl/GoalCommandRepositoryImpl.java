package com.buckwheat.garden.repository.command.impl;

import com.buckwheat.garden.repository.command.GoalCommandRepository;
import com.buckwheat.garden.data.dto.goal.GoalDto;
import com.buckwheat.garden.data.entity.Gardener;
import com.buckwheat.garden.data.entity.Goal;
import com.buckwheat.garden.data.entity.Plant;
import com.buckwheat.garden.dao.GardenerDao;
import com.buckwheat.garden.dao.GoalDao;
import com.buckwheat.garden.dao.PlantDao;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.NoSuchElementException;

@Repository
@RequiredArgsConstructor
@Slf4j
public class GoalCommandRepositoryImpl implements GoalCommandRepository {
    private final GoalDao goalDao;
    private final PlantDao plantDao;
    private final GardenerDao gardenerDao;

    @Override
    public Goal save(Long gardenerId, GoalDto goalRequest) {
        Gardener gardener = gardenerDao.getReferenceById(gardenerId);

        Plant plant = null;

        if (goalRequest.getPlantId() != 0) {
            plant = plantDao.findById(goalRequest.getPlantId()).orElseThrow(NoSuchElementException::new);
        }

        return goalDao.save(goalRequest.toEntityWith(gardener, plant));
    }

    @Override
    public Goal update(GoalDto goalRequest) {
        Plant plant = plantDao.findById(goalRequest.getPlantId()).orElseThrow(NoSuchElementException::new);
        Goal goal = goalDao.findById(goalRequest.getId()).orElseThrow(NoSuchElementException::new);
        goal.update(goalRequest.getContent(), plant);

        return goalDao.save(goal);
    }

    @Override
    public Goal complete(Long goalId) {
        Goal goal = goalDao.findByGoalId(goalId).orElseThrow(NoSuchElementException::new);

        // 들어갈 값 계산
        String complete = goal.getComplete().equals("Y") ? "N" : "Y";
        goal.completeGoal(complete);

        return goalDao.save(goal);
    }

    @Override
    public void deleteBy(Long id) {
        goalDao.deleteById(id);
    }
}
