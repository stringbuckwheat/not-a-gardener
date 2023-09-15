package com.buckwheat.garden.service.impl;

import com.buckwheat.garden.dao.PlantDao;
import com.buckwheat.garden.dao.RoutineDao;
import com.buckwheat.garden.data.dto.garden.GardenMain;
import com.buckwheat.garden.data.dto.garden.GardenResponse;
import com.buckwheat.garden.data.dto.garden.WaitingForWatering;
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
    private final PlantDao plantDao;
    private final RoutineDao routineDao;
    private final GardenResponseProvider gardenResponseProvider;

    @Override
    @Transactional
    public GardenMain getGardenToDo(Long gardenerId) {
        // 저장한 식물이 하나도 없는지
        if (plantDao.getPlantsByGardenerId(gardenerId).size() == 0) {
            return new GardenMain(false, null, null, null);
        }

        List<Plant> waitings = plantDao.getWaitingForWateringList(gardenerId);
        List<WaitingForWatering> waitingList = waitings.stream()
                .map(WaitingForWatering::from).collect(Collectors.toList());

        List<GardenResponse> todoList = new ArrayList<>();
        // waiting for watering list
        for (Plant plant : waitings) {
            todoList.add(gardenResponseProvider.getGardenResponse(Calculate.from(plant, gardenerId)));
        }

        for (RawGarden rawGarden : plantDao.getGarden(gardenerId)) {
            todoList.add(gardenResponseProvider.getGardenResponse(Calculate.from(rawGarden, gardenerId)));
        }

        // 오늘 루틴 리스트
        List<RoutineResponse> routineList = routineDao.getRoutinesByGardenerId(gardenerId).stream()
                .map(RoutineResponse::from)
                .filter(r -> !"N".equals(r.getHasToDoToday()))
                .collect(Collectors.toList());

        return new GardenMain(true, todoList, waitingList, routineList);
    }

    @Override
    public List<GardenResponse> getAll(Long gardenerId) {
        List<GardenResponse> gardenList = new ArrayList<>();

        // 필요한 것들 계산해서 gardenDto list 리턴
        for (Plant plant : plantDao.getPlantsForGarden(gardenerId)) {
            gardenList.add(gardenResponseProvider.getGardenResponse(Calculate.from(plant, gardenerId)));
        }

        return gardenList;
    }
}
