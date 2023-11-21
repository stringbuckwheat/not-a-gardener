package com.buckwheat.garden.goal;

import com.buckwheat.garden.data.entity.Gardener;
import com.buckwheat.garden.data.entity.Goal;
import com.buckwheat.garden.data.entity.Plant;
import com.buckwheat.garden.gardener.GardenerRepository;
import com.buckwheat.garden.plant.PlantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class GoalServiceImpl implements GoalService {
    private final GoalRepository goalRepository;
    private final PlantRepository plantRepository;
    private final GardenerRepository gardenerRepository;

    @Override
    @Transactional(readOnly = true)
    public List<GoalDto> getAll(Long gardenerId) {
        return goalRepository.findByGardener_GardenerId(gardenerId).stream()
                .map(GoalDto::from)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public GoalDto add(Long gardenerId, GoalDto goalRequest) {
        Gardener gardener = gardenerRepository.getReferenceById(gardenerId);

        Plant plant = null;

        if (goalRequest.getPlantId() != 0) {
            plant = plantRepository.findById(goalRequest.getPlantId()).orElseThrow(NoSuchElementException::new);
        }

        Goal goal = goalRepository.save(goalRequest.toEntityWith(gardener, plant));
        return GoalDto.from(goal);
    }

    @Override
    @Transactional
    public GoalDto update(GoalDto goalRequest) {
        Plant plant = plantRepository.findById(goalRequest.getPlantId()).orElseThrow(NoSuchElementException::new);
        Goal goal = goalRepository.findById(goalRequest.getId()).orElseThrow(NoSuchElementException::new);
        goal.update(goalRequest.getContent(), plant);

        return GoalDto.from(goal);
    }

    @Override
    @Transactional
    public GoalDto complete(Long id) {
        Goal goal = goalRepository.findByGoalId(id).orElseThrow(NoSuchElementException::new);

        // 들어갈 값 계산
        String complete = goal.getComplete().equals("Y") ? "N" : "Y";
        goal.completeGoal(complete);

        return GoalDto.from(goal);
    }

    @Override
    public void delete(Long id) {
        goalRepository.deleteById(id);
    }
}
