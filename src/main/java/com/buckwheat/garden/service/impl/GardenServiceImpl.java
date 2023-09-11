package com.buckwheat.garden.service.impl;

import com.buckwheat.garden.dao.PlantDao;
import com.buckwheat.garden.dao.RoutineDao;
import com.buckwheat.garden.data.dto.GardenDto;
import com.buckwheat.garden.data.dto.RoutineDto;
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
    public GardenDto.GardenMain getGardenToDo(Long gardenerId) {
        // 저장한 식물이 하나도 없는지
        if (plantDao.getPlantsByGardenerId(gardenerId).size() == 0) {
            return new GardenDto.GardenMain(false, null, null, null);
        }

        List<Plant> waitings = plantDao.getWaitingForWateringList(gardenerId);
        List<GardenDto.WaitingForWatering> waitingList = waitings.stream()
                .map(GardenDto.WaitingForWatering::from).collect(Collectors.toList());

        List<GardenDto.Response> todoList = new ArrayList<>();
        // waiting for watering list
        for (Plant plant : waitings) {
            todoList.add(gardenResponseProvider.getGardenResponse(Calculate.from(plant, gardenerId)));
        }

        for (RawGarden rawGarden : plantDao.getGarden(gardenerId)) {
            todoList.add(gardenResponseProvider.getGardenResponse(Calculate.from(rawGarden, gardenerId)));
        }

        // 오늘 루틴 리스트
        List<RoutineDto.Response> routineList = routineDao.getRoutinesByGardenerId(gardenerId).stream()
                .map(RoutineDto.Response::from)
                .filter(r -> !"N".equals(r.getHasToDoToday()))
                .collect(Collectors.toList());

        return new GardenDto.GardenMain(true, todoList, waitingList, routineList);
    }

    @Override
    public List<GardenDto.Response> getAll(Long gardenerId) {
        List<GardenDto.Response> gardenList = new ArrayList<>();

        // 필요한 것들 계산해서 gardenDto list 리턴
        for (Plant plant : plantDao.getPlantsForGarden(gardenerId)) {
            gardenList.add(gardenResponseProvider.getGardenResponse(Calculate.from(plant, gardenerId)));
        }

        return gardenList;
    }
}
