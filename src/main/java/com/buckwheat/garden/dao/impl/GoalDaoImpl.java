package com.buckwheat.garden.dao.impl;

import com.buckwheat.garden.dao.GoalDao;
import com.buckwheat.garden.data.dto.goal.GoalDto;
import com.buckwheat.garden.data.entity.Gardener;
import com.buckwheat.garden.data.entity.Goal;
import com.buckwheat.garden.data.entity.Plant;
import com.buckwheat.garden.repository.GardenerRepository;
import com.buckwheat.garden.repository.GoalRepository;
import com.buckwheat.garden.repository.PlantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.NoSuchElementException;

@Repository
@RequiredArgsConstructor
@Slf4j
public class GoalDaoImpl implements GoalDao {
    private final GoalRepository goalRepository;
    private final PlantRepository plantRepository;
    private final GardenerRepository gardenerRepository;

    @Override
    public List<Goal> getGoalListBy(Long gardenerId) {
        return goalRepository.findByGardener_GardenerId(gardenerId);
    }

    @Override
    public Goal save(Long gardenerId, GoalDto goalRequest) {
        Gardener gardener = gardenerRepository.getReferenceById(gardenerId);

        Plant plant = null;

        if (goalRequest.getPlantId() != 0) {
            plant = plantRepository.findById(goalRequest.getPlantId()).orElseThrow(NoSuchElementException::new);
        }

        return goalRepository.save(goalRequest.toEntityWith(gardener, plant));
    }

    @Override
    public Goal update(GoalDto goalRequest) {
        Plant plant = plantRepository.findById(goalRequest.getPlantId()).orElseThrow(NoSuchElementException::new);
        Goal goal = goalRepository.findById(goalRequest.getId()).orElseThrow(NoSuchElementException::new);
        goal.update(goalRequest.getContent(), plant);

        return goalRepository.save(goal);
    }

    @Override
    public Goal complete(Long goalId) {
        Goal goal = goalRepository.findByGoalId(goalId).orElseThrow(NoSuchElementException::new);

        // 들어갈 값 계산
        String complete = goal.getComplete().equals("Y") ? "N" : "Y";
        goal.completeGoal(complete);

        return goalRepository.save(goal);
    }

    @Override
    public void deleteBy(Long id) {
        goalRepository.deleteById(id);
    }
}
