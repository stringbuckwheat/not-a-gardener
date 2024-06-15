package xyz.notagardener.routine.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.notagardener.common.error.code.ExceptionCode;
import xyz.notagardener.common.error.exception.ResourceNotFoundException;
import xyz.notagardener.common.error.exception.UnauthorizedAccessException;
import xyz.notagardener.gardener.model.Gardener;
import xyz.notagardener.gardener.repository.GardenerRepository;
import xyz.notagardener.plant.Plant;
import xyz.notagardener.plant.plant.repository.PlantRepository;
import xyz.notagardener.routine.Routine;
import xyz.notagardener.routine.repository.RoutineRepository;
import xyz.notagardener.routine.dto.RoutineComplete;
import xyz.notagardener.routine.dto.RoutineMain;
import xyz.notagardener.routine.dto.RoutineRequest;
import xyz.notagardener.routine.dto.RoutineResponse;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class RoutineServiceImpl implements RoutineService {
    private final RoutineRepository routineRepository;
    private final PlantRepository plantRepository;
    private final GardenerRepository gardenerRepository;

    private Plant getPlantByPlantIdAndGardenerId(Long plantId, Long gardenerId) {
        Plant plant = plantRepository.findById(plantId)
                .orElseThrow(() -> new ResourceNotFoundException(ExceptionCode.NO_SUCH_PLANT));

        if (!plant.getGardener().getGardenerId().equals(gardenerId)) {
            throw new UnauthorizedAccessException(ExceptionCode.NOT_YOUR_PLANT);
        }

        return plant;
    }

    private Routine getRoutineByRoutineIdAndGardenerId(Long routineId, Long gardenerId) {
        Routine routine = routineRepository.findByRoutineId(routineId)
                .orElseThrow(() -> new ResourceNotFoundException(ExceptionCode.NO_SUCH_ROUTINE));

        if (!routine.getGardener().getGardenerId().equals(gardenerId)) {
            throw new UnauthorizedAccessException(ExceptionCode.NOT_YOUR_ROUTINE);
        }

        return routine;
    }

    @Override
    @Transactional(readOnly = true)
    public RoutineMain getAll(Long gardenerId) {
        List<RoutineResponse> routines = routineRepository.findByGardener_GardenerId(gardenerId)
                .stream()
                .map(RoutineResponse::new)
                .toList();

        List<RoutineResponse> toDoList = routines.stream()
                .filter(routine -> routine.getHasToDoToday().equals("Y"))
                .toList();

        List<RoutineResponse> notToDoList = routines.stream()
                .filter(routine -> !routine.getHasToDoToday().equals("Y"))
                .toList();

        return new RoutineMain(toDoList, notToDoList);
    }

    @Override
    @Transactional
    public RoutineResponse add(RoutineRequest routineRequest, Long gardenerId) {
        Gardener gardener = gardenerRepository.getReferenceById(gardenerId);
        Plant plant = getPlantByPlantIdAndGardenerId(routineRequest.getPlantId(), gardenerId);
        Routine routine = routineRepository.save(routineRequest.toEntityWith(plant, gardener));

        return new RoutineResponse(routine);
    }

    @Override
    @Transactional
    public RoutineResponse update(RoutineRequest routineRequest, Long gardenerId) {
        Plant plant = getPlantByPlantIdAndGardenerId(routineRequest.getPlantId(), gardenerId);
        Routine routine = getRoutineByRoutineIdAndGardenerId(routineRequest.getId(), gardenerId);
        routine.update(routineRequest.getContent(), routineRequest.getCycle(), plant);

        return new RoutineResponse(routine);
    }

    @Override
    @Transactional
    public RoutineResponse complete(RoutineComplete routineComplete, Long gardenerId) {
        Routine routine = getRoutineByRoutineIdAndGardenerId(routineComplete.getId(), gardenerId);
        routine.complete(routineComplete.getLastCompleteDate());

        return new RoutineResponse(routine);
    }

    @Override
    public void delete(Long routineId, Long gardenerId) {
        Routine routine = getRoutineByRoutineIdAndGardenerId(routineId, gardenerId);
        routineRepository.delete(routine);
    }
}
