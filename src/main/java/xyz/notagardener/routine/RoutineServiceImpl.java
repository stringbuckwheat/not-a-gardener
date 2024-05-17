package xyz.notagardener.routine;

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
import xyz.notagardener.plant.plant.repository.PlantRepository;
import xyz.notagardener.routine.dto.RoutineComplete;
import xyz.notagardener.routine.dto.RoutineMain;
import xyz.notagardener.routine.dto.RoutineRequest;
import xyz.notagardener.routine.dto.RoutineResponse;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class RoutineServiceImpl implements RoutineService {
    private final RoutineRepository routineRepository;
    private final PlantRepository plantRepository;
    private final GardenerRepository gardenerRepository;

    private Gardener getGardenerByGardenerId(Long gardenerId) {
        Gardener gardener = gardenerRepository.getReferenceById(gardenerId);

        if (gardener == null) {
            throw new UsernameNotFoundException(ExceptionCode.NO_ACCOUNT.getCode());
        }

        return gardener;
    }

    private Plant getPlantByPlantIdAndGardenerId(Long plantId, Long gardenerId) {
        Plant plant = plantRepository.findById(plantId)
                .orElseThrow(() -> new NoSuchElementException(ExceptionCode.NO_SUCH_ITEM.getCode()));

        if (!plant.getGardener().getGardenerId().equals(gardenerId)) {
            throw new UnauthorizedAccessException(ExceptionCode.NOT_YOUR_THING.getCode());
        }

        return plant;
    }

    private Routine getRoutineByRoutineIdAndGardenerId(Long routineId, Long gardenerId) {
        Routine routine = routineRepository.findByRoutineId(routineId)
                .orElseThrow(() -> new NoSuchElementException(ExceptionCode.NO_SUCH_ITEM.getCode()));

        if (!routine.getGardener().getGardenerId().equals(gardenerId)) {
            throw new UnauthorizedAccessException(ExceptionCode.NOT_YOUR_THING.getCode());
        }

        return routine;
    }

    @Override
    @Transactional(readOnly = true)
    public RoutineMain getAll(Long gardenerId) {
        List<RoutineResponse> routines = routineRepository.findByGardener_GardenerId(gardenerId)
                .stream()
                .map(RoutineResponse::from)
                .collect(Collectors.toList());

        List<RoutineResponse> toDoList = routines.stream()
                .filter(routine -> routine.getHasToDoToday().equals("Y"))
                .collect(Collectors.toList());

        List<RoutineResponse> notToDoList = routines.stream()
                .filter(routine -> !routine.getHasToDoToday().equals("Y"))
                .collect(Collectors.toList());

        return new RoutineMain(toDoList, notToDoList);
    }

    @Override
    @Transactional
    public RoutineResponse add(RoutineRequest routineRequest, Long gardenerId) {
        if (!routineRequest.isValidForSave()) {
            throw new IllegalArgumentException(ExceptionCode.INVALID_REQUEST_DATA.getCode());
        }

        Gardener gardener = getGardenerByGardenerId(gardenerId);
        Plant plant = getPlantByPlantIdAndGardenerId(routineRequest.getPlantId(), gardenerId);
        Routine routine = routineRepository.save(routineRequest.toEntityWith(plant, gardener));

        return RoutineResponse.from(routine);
    }

    @Override
    @Transactional
    public RoutineResponse update(RoutineRequest routineRequest, Long gardenerId) {
        if (!routineRequest.isValidForUpdate()) {
            throw new IllegalArgumentException(ExceptionCode.INVALID_REQUEST_DATA.getCode());
        }

        Plant plant = getPlantByPlantIdAndGardenerId(routineRequest.getPlantId(), gardenerId);
        Routine routine = getRoutineByRoutineIdAndGardenerId(routineRequest.getId(), gardenerId);
        routine.update(routineRequest.getContent(), routineRequest.getCycle(), plant);

        return RoutineResponse.from(routine);
    }

    @Override
    @Transactional
    public RoutineResponse complete(RoutineComplete routineComplete, Long gardenerId) {
        Routine routine = getRoutineByRoutineIdAndGardenerId(routineComplete.getId(), gardenerId);
        routine.complete(routineComplete.getLastCompleteDate());

        return RoutineResponse.from(routine);
    }

    @Override
    public void delete(Long routineId, Long gardenerId) {
        Routine routine = getRoutineByRoutineIdAndGardenerId(routineId, gardenerId);
        routineRepository.delete(routine);
    }
}
