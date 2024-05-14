package xyz.notagardener.goal;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.notagardener.common.error.code.ExceptionCode;
import xyz.notagardener.common.error.exception.UnauthorizedAccessException;
import xyz.notagardener.gardener.Gardener;
import xyz.notagardener.gardener.gardener.GardenerRepository;
import xyz.notagardener.plant.Plant;
import xyz.notagardener.plant.repository.PlantRepository;

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

    public Plant getPlantForGoal(Long plantId, Long gardenerId) {
        Plant plant = plantRepository.findByPlantId(plantId)
                .orElseThrow(() -> new NoSuchElementException(ExceptionCode.NO_SUCH_ITEM.getCode()));

        if(!plant.getGardener().getGardenerId().equals(gardenerId)) {
            throw new UnauthorizedAccessException(ExceptionCode.NOT_YOUR_THING.getCode());
        }

        return plant;
    }

    public Goal getGoalByGoalIdAndGardenerId(Long goalId, Long gardenerId) {
        Goal goal = goalRepository.findByGoalId(goalId)
                .orElseThrow(() -> new NoSuchElementException(ExceptionCode.NO_SUCH_ITEM.getCode()));

        if(goal.getGardener().getGardenerId() != gardenerId) {
            throw new UnauthorizedAccessException(ExceptionCode.NOT_YOUR_THING.getCode());
        }

        return goal;
    }

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
        // 입력값 유효성 검증
        if(!goalRequest.isValidForSave()) {
            throw new IllegalArgumentException(ExceptionCode.INVALID_REQUEST_DATA.getCode());
        }

        Gardener gardener = gardenerRepository.getReferenceById(gardenerId);

        if (gardener == null) {
            throw new UsernameNotFoundException(ExceptionCode.NO_ACCOUNT.getCode());
        }

        Plant plant = null;

        if (goalRequest.getPlantId() != 0) {
            plant = getPlantForGoal(goalRequest.getPlantId(), gardenerId);
        }

        Goal goal = goalRepository.save(goalRequest.toEntityWith(gardener, plant));
        return GoalDto.from(goal);
    }

    @Override
    @Transactional
    public GoalDto update(Long gardenerId, GoalDto goalRequest) {
        // 입력값 유효성 검증
        if(!goalRequest.isValidForUpdate()) {
            throw new IllegalArgumentException(ExceptionCode.INVALID_REQUEST_DATA.getCode());
        }

        // Plant == nullable
        Plant plant = null;

        if (goalRequest.getPlantId() != 0) {
            plant = getPlantForGoal(goalRequest.getPlantId(), gardenerId);
        }

        // 기존 목표 엔티티 찾기
        Goal goal = getGoalByGoalIdAndGardenerId(goalRequest.getId(), gardenerId);

        // 수정
        goal.update(goalRequest.getContent(), plant);

        return GoalDto.from(goal);
    }

    @Override
    @Transactional
    public GoalDto complete(Long goalId, Long gardenerId) {
        Goal goal = getGoalByGoalIdAndGardenerId(goalId, gardenerId);

        // 들어갈 값 계산
        String complete = goal.getComplete().equals("Y") ? "N" : "Y";
        goal.completeGoal(complete);

        return GoalDto.from(goal);
    }

    @Override
    public void delete(Long goalId, Long gardenerId) {
        Goal goal = getGoalByGoalIdAndGardenerId(goalId, gardenerId);
        goalRepository.delete(goal);
    }
}
