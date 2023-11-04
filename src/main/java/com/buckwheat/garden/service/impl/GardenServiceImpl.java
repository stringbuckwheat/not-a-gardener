package com.buckwheat.garden.service.impl;

import com.buckwheat.garden.dao.GardenDao;
import com.buckwheat.garden.data.dto.garden.GardenMain;
import com.buckwheat.garden.data.dto.garden.GardenResponse;
import com.buckwheat.garden.data.dto.garden.RawGardenMain;
import com.buckwheat.garden.data.dto.routine.RoutineResponse;
import com.buckwheat.garden.data.entity.Plant;
import com.buckwheat.garden.data.projection.Calculate;
import com.buckwheat.garden.data.projection.RawGarden;
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
    private final GardenDao gardenDao;

    @Override
    @Transactional
    public GardenMain readGarden(Long gardenerId) {
        if (!gardenDao.hasAnyPlant(gardenerId)) {
            return new GardenMain(false, null, null, null);
        }

        RawGardenMain rawGardenMain = gardenDao.findForGardenMain(gardenerId);

        List<GardenResponse> todoList = new ArrayList<>();

        for (RawGarden rawGarden : rawGardenMain.getPlantsToDo()) {
            todoList.add(gardenResponseProvider.getGardenResponse(Calculate.from(rawGarden, gardenerId)));
        }

        // 오늘 루틴 리스트
        List<RoutineResponse> routines = rawGardenMain.getRoutines().stream()
                .map(RoutineResponse::from)
                .filter(r -> !"N".equals(r.getHasToDoToday()))
                .collect(Collectors.toList());

        return new GardenMain(true, todoList, rawGardenMain.getWaitingForWatering(), routines);
    }

    @Override
    public List<GardenResponse> readAll(Long gardenerId) {
        List<GardenResponse> gardenList = new ArrayList<>();

        // 필요한 것들 계산해서 gardenDto list 리턴
        for (Plant plant : gardenDao.findAllPlants(gardenerId)) {
            gardenList.add(gardenResponseProvider.getGardenResponse(Calculate.from(plant, gardenerId)));
        }

        return gardenList;
    }
}
