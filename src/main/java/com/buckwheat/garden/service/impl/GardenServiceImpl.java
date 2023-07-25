package com.buckwheat.garden.service.impl;

import com.buckwheat.garden.dao.PlantDao;
import com.buckwheat.garden.dao.RoutineDao;
import com.buckwheat.garden.data.dto.Calculate;
import com.buckwheat.garden.data.dto.GardenDto;
import com.buckwheat.garden.data.dto.RawGarden;
import com.buckwheat.garden.data.dto.RoutineDto;
import com.buckwheat.garden.data.entity.Plant;
import com.buckwheat.garden.data.entity.Routine;
import com.buckwheat.garden.service.GardenService;
import com.buckwheat.garden.util.RoutineUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class GardenServiceImpl implements GardenService {
    private final PlantDao plantDao;
    private final RoutineDao routineDao;
    private final RoutineUtil routineUtil;
    private final GardenResponseProvider gardenResponseProvider;

    @Override
    @Transactional
    public GardenDto.GardenMain getGarden(Long gardenerId) {
        // 저장한 식물이 하나도 없는지
        if (plantDao.getPlantsByGardenerId(gardenerId).size() == 0) {
            return new GardenDto.GardenMain(false, null, null, null);
        }

        List<GardenDto.Response> todoList = new ArrayList<>();
        List<GardenDto.WaitingForWatering> waitingList = new ArrayList<>();
        LocalDate today = LocalDate.now();

        // waiting for watering list
        for (Plant plant : plantDao.getWaitingForWateringList(gardenerId)) {
            waitingList.add(GardenDto.WaitingForWatering.from(plant));
            todoList.add(gardenResponseProvider.getGardenResponse(Calculate.from(plant, gardenerId)));
        }

        // todolist
        for (RawGarden rawGarden : plantDao.getGarden(gardenerId)) {
            todoList.add(gardenResponseProvider.getGardenResponse(Calculate.from(rawGarden, gardenerId)));
        }

        // 오늘 루틴 리스트
        List<RoutineDto.Response> routineList = getRoutinesForToday(gardenerId);

        return new GardenDto.GardenMain(true, todoList, waitingList, routineList);
    }

    public List<RoutineDto.Response> getRoutinesForToday(Long gardenerId) {
        List<RoutineDto.Response> routineList = new ArrayList<>();
        LocalDateTime today = LocalDate.now().atStartOfDay();

        // 루틴 띄우기
        for (Routine routine : routineDao.getRoutinesByGardenerId(gardenerId)) {
            // 오늘 해야 하는 루틴인지 계산
            String hasTodoToday = routineUtil.hasToDoToday(routine, today);

            if (hasTodoToday.equals("N")) {
                continue;
            }

            String isCompleted = routineUtil.isCompleted(routine.getLastCompleteDate(), today);
            routineList.add(RoutineDto.Response.from(routine, hasTodoToday, isCompleted));
        }

        return routineList;
    }

    @Override
    public List<GardenDto.Response> getPlantsByGardenerId(Long gardenerId) {
        List<GardenDto.Response> gardenList = new ArrayList<>();

        // 필요한 것들 계산해서 gardenDto list 리턴
        for (Plant plant : plantDao.getPlantsForGarden(gardenerId)) {
            gardenList.add(gardenResponseProvider.getGardenResponse(Calculate.from(plant, gardenerId)));
        }

        return gardenList;
    }
}
