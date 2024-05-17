package xyz.notagardener.plant.garden.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.notagardener.plant.garden.dto.GardenMain;
import xyz.notagardener.plant.garden.dto.GardenResponse;
import xyz.notagardener.plant.garden.dto.RawGarden;
import xyz.notagardener.plant.garden.dto.WaitingForWatering;
import xyz.notagardener.plant.plant.repository.PlantRepository;
import xyz.notagardener.routine.Routine;
import xyz.notagardener.routine.RoutineRepository;
import xyz.notagardener.routine.dto.RoutineResponse;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class GardenServiceImpl implements GardenService {
    private final GardenResponseMapper gardenResponseMapper;
    private final PlantRepository plantRepository;
    private final RoutineRepository routineRepository;

    @Override
    @Transactional(readOnly = true)
    public GardenMain getGarden(Long gardenerId) {
        if (!plantRepository.existByGardenerId(gardenerId)) {
            return new GardenMain(false, null, null, null);
        }

        List<WaitingForWatering> waitingForWatering = plantRepository.findWaitingForWateringList(gardenerId);
        List<RawGarden> plantsToDo = plantRepository.findGardenByGardenerId(gardenerId);
        List<Routine> routineList = routineRepository.findByGardener_GardenerId(gardenerId);

        List<GardenResponse> todoList = plantsToDo.stream()
                .map(rawGarden -> gardenResponseMapper.getGardenResponse(rawGarden, gardenerId))
                .collect(Collectors.toList());

        // 오늘 루틴 리스트
        List<RoutineResponse> routines = routineList.stream()
                .map(RoutineResponse::from)
                .filter(r -> !"N".equals(r.getHasToDoToday()))
                .collect(Collectors.toList());

        return new GardenMain(true, todoList, waitingForWatering, routines);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GardenResponse> getAll(Long gardenerId) {
        return plantRepository.findAllPlantsWithLatestWateringDate(gardenerId).stream()
                .map(rawGarden -> gardenResponseMapper.getGardenResponse((RawGarden) rawGarden, gardenerId))
                .collect(Collectors.toList());
    }
}
