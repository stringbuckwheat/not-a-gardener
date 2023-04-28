package com.buckwheat.garden.dao.impl;

import com.buckwheat.garden.dao.GoalDao;
import com.buckwheat.garden.data.dto.GoalDto;
import com.buckwheat.garden.data.entity.Gardener;
import com.buckwheat.garden.data.entity.Goal;
import com.buckwheat.garden.data.entity.Plant;
import com.buckwheat.garden.repository.GardenerRepository;
import com.buckwheat.garden.repository.GoalRepository;
import com.buckwheat.garden.repository.PlantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.NoSuchElementException;

@Repository
@RequiredArgsConstructor
public class GoalDaoImpl implements GoalDao {
    private final GoalRepository goalRepository;
    private final PlantRepository plantRepository;
    private final GardenerRepository gardenerRepository;

    @Override
    public List<Goal> getGoalListBy(Long gardenerId){
        return goalRepository.findByGardener_GardenerId(gardenerId);
    }

    @Override
    public Goal save(Long gardenerId, GoalDto.Request goalRequest) {
        Gardener gardener = gardenerRepository.findById(gardenerId).orElseThrow(NoSuchElementException::new);
        Plant plant = plantRepository.findById(goalRequest.getPlantId()).orElse(null);

        return goalRepository.save(goalRequest.toEntityWith(gardener, plant));
    }

    @Override
    public Goal update(GoalDto.Request goalRequest) {
        Plant plant = plantRepository.findById(goalRequest.getPlantId()).orElseThrow(NoSuchElementException::new);
        Goal prevGoal = goalRepository.findById(goalRequest.getId()).orElseThrow(NoSuchElementException::new);

        return goalRepository.save(prevGoal.update(goalRequest.getContent(), plant));
    }

    @Override
    public Goal complete(Long goalId){
        Goal prevGoal = goalRepository.findByGoalId(goalId).orElseThrow(NoSuchElementException::new);

        // 들어갈 값 계산
        String complete = prevGoal.getComplete().equals("Y") ? "N" : "Y";

        return goalRepository.save(prevGoal.completeGoal(complete));
    }

    @Override
    public void deleteBy(Long id){
        goalRepository.deleteById(id);
    }
}
