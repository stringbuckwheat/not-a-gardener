package xyz.notagardener.domain.plant.service;

import xyz.notagardener.domain.plant.dto.garden.GardenMain;
import xyz.notagardener.domain.plant.dto.garden.GardenResponse;
import xyz.notagardener.domain.plant.dto.garden.WaitingForWatering;
import xyz.notagardener.domain.plant.dto.projection.Calculate;
import xyz.notagardener.domain.plant.dto.projection.RawGarden;
import xyz.notagardener.domain.plant.repository.PlantRepository;
import xyz.notagardener.domain.routine.Routine;
import xyz.notagardener.domain.routine.RoutineRepository;
import xyz.notagardener.domain.routine.dto.RoutineResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
                .map(rawGarden -> gardenResponseMapper.getGardenResponse(Calculate.from(rawGarden, gardenerId)))
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
        return plantRepository.findByGardener_GardenerIdOrderByPlantIdDesc(gardenerId).stream()
                .map(plant -> gardenResponseMapper.getGardenResponse(Calculate.from(plant, gardenerId)))
                .collect(Collectors.toList());
    }
}
