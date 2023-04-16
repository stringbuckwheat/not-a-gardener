package com.buckwheat.garden.service.impl;

import com.buckwheat.garden.code.AfterWateringCode;
import com.buckwheat.garden.code.WateringCode;
import com.buckwheat.garden.dao.PlantDao;
import com.buckwheat.garden.dao.RoutineDao;
import com.buckwheat.garden.dao.WateringDao;
import com.buckwheat.garden.data.dto.GardenDto;
import com.buckwheat.garden.data.dto.PlantDto;
import com.buckwheat.garden.data.dto.RoutineDto;
import com.buckwheat.garden.data.dto.WateringDto;
import com.buckwheat.garden.data.entity.Member;
import com.buckwheat.garden.data.entity.Plant;
import com.buckwheat.garden.data.entity.Routine;
import com.buckwheat.garden.data.entity.Watering;
import com.buckwheat.garden.service.GardenService;
import com.buckwheat.garden.util.GardenUtil;
import com.buckwheat.garden.util.RoutineUtil;
import com.buckwheat.garden.util.WateringUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
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
    private final WateringUtil wateringUtil;
    private final RoutineUtil routineUtil;

    @Override
    public GardenDto.GardenMain getGarden(int memberNo) {
        List<GardenDto.Response> todoList = new ArrayList<>();
        List<GardenDto.WaitingForWatering> waitingList = new ArrayList<>();
        LocalDate today = LocalDate.now();

        // 필요한 것들 계산해서 gardenDto list 리턴
        for (Plant plant : plantDao.getPlantListForGarden(memberNo, today)) {
            int wateringDDay = gardenUtil.getWateringDDay(plant.getAverageWateringPeriod(), gardenUtil.getLastDrinkingDay(plant));

            // 미룬 날짜가 오늘이면
            if (plant.getPostponeDate() != null && today.compareTo(plant.getPostponeDate()) == 0) {
                PlantDto.Response plantResponse = PlantDto.Response.from(plant);
                GardenDto.Detail detail = gardenUtil.getGardenDetail(plant, memberNo, wateringDDay, WateringCode.YOU_ARE_LAZY.getCode());

                todoList.add(new GardenDto.Response(plantResponse, detail));
                continue;
            }

            int wateringCode = gardenUtil.getWateringCode(plant.getAverageWateringPeriod(), wateringDDay);

            boolean hasToDo = wateringCode < 0 || wateringCode == WateringCode.THIRSTY.getCode() || wateringCode == WateringCode.CHECK.getCode();

            if (wateringCode == WateringCode.NO_RECORD.getCode()) {
                waitingList.add(GardenDto.WaitingForWatering.from(plant));
                continue;
            } else if (hasToDo) {
                PlantDto.Response plantResponse = PlantDto.Response.from(plant);
                GardenDto.Detail gardenDetail = gardenUtil.getGardenDetail(plant, memberNo, wateringDDay, wateringCode);

                todoList.add(new GardenDto.Response(plantResponse, gardenDetail));
            }
        }

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

            PlantDto.Response plantResponse = PlantDto.Response.from(plant);
            GardenDto.Detail gardenDetail = gardenUtil.getGardenDetail(plant, memberNo);

            gardenList.add(new GardenDto.Response(plantResponse, gardenDetail));
        }

        return gardenList;
    }

    public GardenDto.Response getGardenResponse(int plantNo, int memberNo) {
        Plant plant = plantDao.getPlantWithPlaceAndWatering(plantNo);

        PlantDto.Response plantResponse = PlantDto.Response.from(plant);
        GardenDto.Detail gardenDetail = gardenUtil.getGardenDetail(plant, memberNo);

        return new GardenDto.Response(plantResponse, gardenDetail);
    }

    @Override
    public GardenDto.WateringResponse addWateringInGarden(Member member, WateringDto.Request wateringRequest) {
        Watering watering = wateringDao.addWatering(wateringRequest);

        // 물주기 저장 후 메시지 받아옴
        WateringDto.Message wateringMsg = wateringUtil.getWateringMsg(watering.getPlant().getPlantNo());

        // 필요시 물주기 정보 업데이트
        if (wateringMsg.getAfterWateringCode() != AfterWateringCode.NO_CHANGE.getCode()
                || wateringMsg.getAfterWateringCode() != AfterWateringCode.FIRST_WATERING.getCode()) {
            wateringUtil.updateWateringPeriod(watering.getPlant(), wateringMsg.getAverageWateringDate());
        }

        GardenDto.Response gardenResponse = getGardenResponse(watering.getPlant().getPlantNo(), member.getMemberNo());

        return new GardenDto.WateringResponse(gardenResponse, wateringMsg);
    }

    @Override
    public WateringDto.Message notDry(int plantNo) {
        // 리턴용
        WateringDto.Message wateringMsg = null;

        Plant plant = plantDao.getPlantWithPlaceAndWatering(plantNo);

        // 한 번도 물 준 적 없는 경우
        if (plant.getWateringList().size() == 0) {
            plantDao.updateConditionDate(plant);
            return null;
        }

        // averageWateringPeriod 안 마른 날짜만큼 업데이트
        // 마지막으로 물 준 날짜와 오늘과의 날짜 사이를 계산함
        LocalDate lastDrinkingDate = plant.getWateringList().get(0).getWateringDate();
        int period = (int) Duration.between(lastDrinkingDate.atStartOfDay(), LocalDate.now().atStartOfDay()).toDays();

        if (period + 1 == plant.getAverageWateringPeriod()) {
            // 하루 전에 체크하라고 카드 띄웠는데 not Dry를 누른 경우 -> averageWateringPeriod 변경할 필요 없음
            // 오늘 한정 garden에 안 뜨게 해야함 => conditionDate 오늘 날짜 추가
            plantDao.updateConditionDate(plant);

            // wateringMsg: 물주기 계산에 변동 없음
            wateringMsg = new WateringDto.Message(0, period);
        } else if (period >= plant.getAverageWateringPeriod()) {
            // averageWateringPeriod 업데이트
            // 오늘 한정 garden에 안 뜨게 해야함 => updateDate에 오늘 날짜 추가
            plantDao.update(plant.updateAverageWateringPeriod(period + 1).updateConditionDate());

            // 물주기 늘어나는 중이라는 wateringMsg 만들기
            wateringMsg = new WateringDto.Message(1, period + 1);
        }
        return wateringMsg;
    }

    @Override
    public int postpone(int plantNo) {
        Plant plant = plantDao.getPlantWithPlaceAndWatering(plantNo);
        // 미룰래요(그냥 귀찮아서 물주기 미룬 경우) == averageWateringPeriod 업데이트 안함!!
        // postponeDate를 업데이트함
        plantDao.update(plant.updatePostponeDate());

        // waitingList에서는 오늘 하루만 없애줌
        return WateringCode.YOU_ARE_LAZY.getCode();
    }
}
