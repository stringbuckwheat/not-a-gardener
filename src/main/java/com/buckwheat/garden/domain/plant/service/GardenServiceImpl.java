package com.buckwheat.garden.domain.plant.service;

import com.buckwheat.garden.domain.plant.dto.garden.GardenMain;
import com.buckwheat.garden.domain.plant.dto.garden.GardenResponse;
import com.buckwheat.garden.domain.plant.dto.garden.WaitingForWatering;
import com.buckwheat.garden.domain.plant.dto.projection.Calculate;
import com.buckwheat.garden.domain.plant.dto.projection.RawGarden;
import com.buckwheat.garden.domain.plant.repository.PlantRepository;
import com.buckwheat.garden.domain.routine.Routine;
import com.buckwheat.garden.domain.routine.RoutineRepository;
import com.buckwheat.garden.domain.routine.dto.RoutineResponse;
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
    private final GardenResponseProvider gardenResponseProvider;
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
                .map(rawGarden -> gardenResponseProvider.getGardenResponse(Calculate.from(rawGarden, gardenerId)))
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
                .map(plant -> gardenResponseProvider.getGardenResponse(Calculate.from(plant, gardenerId)))
                .collect(Collectors.toList());
    }
}
