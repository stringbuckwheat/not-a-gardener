package com.buckwheat.garden.service.impl;

import com.buckwheat.garden.data.dto.garden.GardenMain;
import com.buckwheat.garden.data.dto.garden.GardenResponse;
import com.buckwheat.garden.data.dto.garden.WaitingForWatering;
import com.buckwheat.garden.data.dto.routine.RoutineResponse;
import com.buckwheat.garden.data.entity.Plant;
import com.buckwheat.garden.data.entity.Routine;
import com.buckwheat.garden.data.projection.Calculate;
import com.buckwheat.garden.data.projection.RawGarden;
import com.buckwheat.garden.repository.PlantRepository;
import com.buckwheat.garden.repository.RoutineRepository;
import com.buckwheat.garden.service.GardenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
    public GardenMain readGarden(Long gardenerId) {
        if (!plantRepository.existByGardenerId(gardenerId)) {
            return new GardenMain(false, null, null, null);
        }

        List<WaitingForWatering> waitingForWatering = plantRepository.findWaitingForWateringList(gardenerId);
        log.debug("waitingForWateirng.size: {}", waitingForWatering.size());
        List<RawGarden> plantsToDo = plantRepository.findGardenByGardenerId(gardenerId);
        log.debug("plantsTodo.size: {}", plantsToDo.size());
        List<Routine> routineList = routineRepository.findByGardener_GardenerId(gardenerId);

        List<GardenResponse> todoList = new ArrayList<>();

        for (RawGarden rawGarden : plantsToDo) {
            todoList.add(gardenResponseProvider.getGardenResponse(Calculate.from(rawGarden, gardenerId)));
        }

        // 오늘 루틴 리스트
        List<RoutineResponse> routines = routineList.stream()
                .map(RoutineResponse::from)
                .filter(r -> !"N".equals(r.getHasToDoToday()))
                .collect(Collectors.toList());

        return new GardenMain(true, todoList, waitingForWatering, routines);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GardenResponse> readAll(Long gardenerId) {
        List<GardenResponse> gardenList = new ArrayList<>();

        // 필요한 것들 계산해서 gardenDto list 리턴
        for (Plant plant : plantRepository.findByGardener_GardenerIdOrderByPlantIdDesc(gardenerId)) {
            gardenList.add(gardenResponseProvider.getGardenResponse(Calculate.from(plant, gardenerId)));
        }

        return gardenList;
    }
}
