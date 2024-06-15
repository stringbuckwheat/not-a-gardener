package xyz.notagardener.goal.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.notagardener.common.error.code.ExceptionCode;
import xyz.notagardener.common.error.exception.ResourceNotFoundException;
import xyz.notagardener.common.error.exception.UnauthorizedAccessException;
import xyz.notagardener.common.validation.YesOrNoType;
import xyz.notagardener.gardener.model.Gardener;
import xyz.notagardener.gardener.repository.GardenerRepository;
import xyz.notagardener.goal.Goal;
import xyz.notagardener.goal.dto.GoalDto;
import xyz.notagardener.goal.repository.GoalRepository;
import xyz.notagardener.plant.Plant;
import xyz.notagardener.plant.plant.repository.PlantRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class GoalServiceImpl implements GoalService {
    private final GoalRepository goalRepository;
    private final PlantRepository plantRepository;
    private final GardenerRepository gardenerRepository;

    public Plant getPlantForGoal(Long plantId, Long gardenerId) {
        Plant plant = plantRepository.findByPlantId(plantId)
                .orElseThrow(() -> new ResourceNotFoundException(ExceptionCode.NO_SUCH_PLANT));

        if(!plant.getGardener().getGardenerId().equals(gardenerId)) {
            throw new UnauthorizedAccessException(ExceptionCode.NOT_YOUR_PLANT);
        }

        return plant;
    }

    public Goal getGoalByGoalIdAndGardenerId(Long goalId, Long gardenerId) {
        Goal goal = goalRepository.findByGoalId(goalId)
                .orElseThrow(() -> new ResourceNotFoundException(ExceptionCode.NO_SUCH_GOAL));

        if(goal.getGardener().getGardenerId() != gardenerId) {
            throw new UnauthorizedAccessException(ExceptionCode.NOT_YOUR_GOAL);
        }

        return goal;
    }

    @Override
    @Transactional(readOnly = true)
    public List<GoalDto> getAll(Long gardenerId) {
        return goalRepository.findByGardener_GardenerId(gardenerId).stream()
                .map(GoalDto::new)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public GoalDto add(Long gardenerId, GoalDto goalRequest) {
        Gardener gardener = gardenerRepository.getReferenceById(gardenerId);

        Plant plant = null;

        if (goalRequest.getPlantId() != 0) {
            plant = getPlantForGoal(goalRequest.getPlantId(), gardenerId);
        }

        Goal goal = goalRepository.save(goalRequest.toEntityWith(gardener, plant));
        return new GoalDto(goal);
    }

    @Override
    @Transactional
    public GoalDto update(Long gardenerId, GoalDto goalRequest) {
        // Plant == nullable
        Plant plant = null;

        if (goalRequest.getPlantId() != 0) {
            plant = getPlantForGoal(goalRequest.getPlantId(), gardenerId);
        }

        // 기존 목표 엔티티 찾기
        Goal goal = getGoalByGoalIdAndGardenerId(goalRequest.getId(), gardenerId);

        // 수정
        goal.update(goalRequest.getContent(), plant);

        return new GoalDto(goal);
    }

    @Override
    @Transactional
    public GoalDto complete(Long goalId, Long gardenerId) {
        Goal goal = getGoalByGoalIdAndGardenerId(goalId, gardenerId);

        // 들어갈 값 계산
        YesOrNoType complete = goal.getComplete().equals(YesOrNoType.Y) ? YesOrNoType.N : YesOrNoType.Y;
        goal.completeGoal(complete);

        return new GoalDto(goal);
    }

    @Override
    public void delete(Long goalId, Long gardenerId) {
        Goal goal = getGoalByGoalIdAndGardenerId(goalId, gardenerId);
        goalRepository.delete(goal);
    }
}
