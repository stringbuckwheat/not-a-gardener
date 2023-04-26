package com.buckwheat.garden.service.impl;

import com.buckwheat.garden.code.WateringCode;
import com.buckwheat.garden.dao.PlantDao;
import com.buckwheat.garden.dao.RoutineDao;
import com.buckwheat.garden.dao.WateringDao;
import com.buckwheat.garden.data.dto.GardenDto;
import com.buckwheat.garden.data.dto.RoutineDto;
import com.buckwheat.garden.data.entity.Plant;
import com.buckwheat.garden.data.entity.Routine;
import com.buckwheat.garden.service.GardenService;
import com.buckwheat.garden.util.GardenUtil;
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
    private final WateringDao wateringDao;
    private final PlantDao plantDao;
    private final RoutineDao routineDao;
    private final GardenUtil gardenUtil;
    private final RoutineUtil routineUtil;
    private final GardenResponseProvider gardenResponseProvider;

    @Override
    @Transactional
    public GardenDto.GardenMain getGarden(int memberNo) {
        List<GardenDto.Response> todoList = new ArrayList<>();
        List<GardenDto.WaitingForWatering> waitingList = new ArrayList<>();
        LocalDate today = LocalDate.now();

        // 필요한 것들 계산해서 gardenDto list 리턴
        // getPlantListForGarden: postponeDate이 과거거나 아예 미룬 적 없는 식물들
        // 오늘 미룬 식물도 띄워줘야함
        for (Plant plant : plantDao.getPlantListForGarden(memberNo)) {
            if(plant.getConditionDate() != null && today.compareTo(plant.getConditionDate()) == 0){
                continue;
            }

            int wateringDDay = gardenUtil.getWateringDDay(plant.getAverageWateringPeriod(), gardenUtil.getLastDrinkingDay(plant));

            int wateringCode = gardenUtil.getWateringCode(plant.getAverageWateringPeriod(), wateringDDay);
            boolean hasToDo = wateringCode < 0 || wateringCode == WateringCode.THIRSTY.getCode() || wateringCode == WateringCode.CHECK.getCode();

            if (wateringCode == WateringCode.NO_RECORD.getCode()) {
                waitingList.add(GardenDto.WaitingForWatering.from(plant));
                todoList.add(gardenResponseProvider.getGardenResponse(plant, memberNo));
            } else if (hasToDo) {
                todoList.add(gardenResponseProvider.getGardenResponse(plant, memberNo));
            }
        }

        log.debug("todoList: {}", todoList);
        log.debug("waitingList: {}", waitingList);

        // 오늘 루틴 리스트
        List<RoutineDto.Response> routineList = getRoutineListForToday(memberNo);

        return new GardenDto.GardenMain(todoList, waitingList, routineList);
    }

    public List<RoutineDto.Response> getRoutineListForToday(int memberNo) {
        List<RoutineDto.Response> routineList = new ArrayList<>();
        LocalDateTime today = LocalDate.now().atStartOfDay();

        // 루틴 띄우기
        for (Routine routine : routineDao.getRoutineListByMemberNo(memberNo)) {
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
    public List<GardenDto.Response> getPlantList(int memberNo) {
        List<GardenDto.Response> gardenList = new ArrayList<>();

        // 필요한 것들 계산해서 gardenDto list 리턴
        for (Plant plant : plantDao.getPlantListForGarden(memberNo)) {
            gardenList.add(gardenResponseProvider.getGardenResponse(plant, memberNo));
        }

        return gardenList;
    }
}
