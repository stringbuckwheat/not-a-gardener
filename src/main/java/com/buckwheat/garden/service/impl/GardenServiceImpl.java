package com.buckwheat.garden.service.impl;

import com.buckwheat.garden.data.dto.GardenDto;
import com.buckwheat.garden.data.dto.PlantDto;
import com.buckwheat.garden.data.dto.RoutineDto;
import com.buckwheat.garden.data.dto.WateringDto;
import com.buckwheat.garden.data.entity.Member;
import com.buckwheat.garden.data.entity.Plant;
import com.buckwheat.garden.data.entity.Routine;
import com.buckwheat.garden.repository.PlantRepository;
import com.buckwheat.garden.repository.RoutineRepository;
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
import java.util.NoSuchElementException;

@Slf4j
@Service
@RequiredArgsConstructor
public class GardenServiceImpl implements GardenService {
    private final PlantRepository plantRepository;
    private final RoutineRepository routineRepository;

    private final GardenUtil gardenUtil;
    private final WateringUtil wateringUtil;
    private final RoutineUtil routineUtil;

    @Override
    public GardenDto.GardenMain getGarden(int memberNo) {
        List<GardenDto.GardenResponse> todoList = new ArrayList<>();
        List<GardenDto.WaitingForWatering> waitingList = new ArrayList<>();
        LocalDate today = LocalDate.now();

        List<Plant> plantList = plantRepository.findByMember_MemberNoAndConditionDateIsBeforeOrConditionDateIsNull(memberNo, today);

        // 필요한 것들 계산해서 gardenDto list 리턴
        for (Plant plant : plantList) {
            int wateringDDay = gardenUtil.getWateringDDay(plant.getAverageWateringPeriod(), gardenUtil.getLastDrinkingDay(plant));

            // 미룬 날짜가 오늘이면
            if(plant.getPostponeDate() != null && today.compareTo(plant.getPostponeDate()) == 0){
                PlantDto.PlantResponse plantResponse = PlantDto.PlantResponse.from(plant);
                GardenDto.GardenDetail gardenDetail = gardenUtil.getGardenDetail(plant, memberNo, wateringDDay, 6);

                todoList.add(new GardenDto.GardenResponse(plantResponse, gardenDetail));
                continue;
            }

            int wateringCode = gardenUtil.getWateringCode(plant.getAverageWateringPeriod(), wateringDDay);

            boolean hasToDo = wateringCode < 0 || wateringCode == 1 || wateringCode == 2;

            if (wateringCode == 0) {
                waitingList.add(GardenDto.WaitingForWatering.from(plant));
                continue;
            } else if (hasToDo) {
                PlantDto.PlantResponse plantResponse = PlantDto.PlantResponse.from(plant);
                GardenDto.GardenDetail gardenDetail = gardenUtil.getGardenDetail(plant, memberNo, wateringDDay, wateringCode);

                todoList.add(new GardenDto.GardenResponse(plantResponse, gardenDetail));
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
        for (Routine routine : routineRepository.findByMember_MemberNo(memberNo)) {
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
    public List<GardenDto.GardenResponse> getPlantList(int memberNo) {
        List<GardenDto.GardenResponse> gardenList = new ArrayList<>();

        List<Plant> plantList = plantRepository.findByMember_MemberNo(memberNo);

        // 필요한 것들 계산해서 gardenDto list 리턴
        for (Plant plant : plantList) {

            PlantDto.PlantResponse plantResponse = PlantDto.PlantResponse.from(plant);
            GardenDto.GardenDetail gardenDetail = gardenUtil.getGardenDetail(plant, memberNo);

            gardenList.add(new GardenDto.GardenResponse(plantResponse, gardenDetail));
        }

        return gardenList;
    }

    public GardenDto.GardenResponse getGardenResponse(int plantNo, int memberNo) {
        Plant plant = plantRepository.findByPlantNo(plantNo).orElseThrow(NoSuchElementException::new);

        PlantDto.PlantResponse plantResponse = PlantDto.PlantResponse.from(plant);
        GardenDto.GardenDetail gardenDetail = gardenUtil.getGardenDetail(plant, memberNo);

        return new GardenDto.GardenResponse(plantResponse, gardenDetail);
    }

    @Override
    public GardenDto.WateringResponse addWateringInGarden(Member member, WateringDto.WateringRequest wateringRequest) {
        Plant plant = plantRepository.findByPlantNo(wateringRequest.getPlantNo())
                .orElseThrow(NoSuchElementException::new);
        log.debug("plant: {}", PlantDto.PlantResponse.from(plant));

        // 물주기 저장 후 메시지 받아옴
        WateringDto.WateringMsg wateringMsg = wateringUtil.addWatering(plant, wateringRequest);

        // 필요시 물주기 정보 업데이트
        wateringUtil.getPlantForAdd(plant, wateringMsg.getAverageWateringDate());

        GardenDto.GardenResponse gardenResponse = getGardenResponse(plant.getPlantNo(), member.getMemberNo());

        return new GardenDto.WateringResponse(gardenResponse, wateringMsg);
    }

    @Override
    public WateringDto.WateringMsg notDry(int plantNo) {
        // 리턴용
        WateringDto.WateringMsg wateringMsg = null;

        Plant plant = plantRepository.findByPlantNo(plantNo).orElseThrow(NoSuchElementException::new);

        // 한 번도 물 준 적 없는 경우
        if(plant.getWateringList().size() == 0){
            plantRepository.save(plant.updateConditionDate());
            return null;
        }

        // averageWateringPeriod 안 마른 날짜만큼 업데이트
        // 마지막으로 물 준 날짜와 오늘과의 날짜 사이를 계산함
        LocalDate lastDrinkingDate = plant.getWateringList().get(0).getWateringDate();
        int period = (int) Duration.between(lastDrinkingDate.atStartOfDay(), LocalDate.now().atStartOfDay()).toDays();

        if (period + 1 == plant.getAverageWateringPeriod()) {
            // 하루 전에 체크하라고 카드 띄웠는데 not Dry를 누른 경우 -> averageWateringPeriod 변경할 필요 없음
            // 오늘 한정 garden에 안 뜨게 해야함 => conditionDate 오늘 날짜 추가
            plantRepository.save(plant.updateConditionDate());

            // wateringMsg: 물주기 계산에 변동 없음
            wateringMsg = new WateringDto.WateringMsg(0, period);
        } else if (period >= plant.getAverageWateringPeriod()) {
            // averageWateringPeriod 업데이트
            // 오늘 한정 garden에 안 뜨게 해야함 => updateDate에 오늘 날짜 추가
            plantRepository.save(plant.updateAverageWateringPeriod(period + 1).updateConditionDate());

            // 물주기 늘어나는 중이라는 wateringMsg 만들기
            wateringMsg = new WateringDto.WateringMsg(1, period + 1);
        }
        return wateringMsg;
    }

    @Override
    public int postpone(int plantNo) {
        Plant plant = plantRepository.findByPlantNo(plantNo).orElseThrow(NoSuchElementException::new);
        // 미룰래요(그냥 귀찮아서 물주기 미룬 경우) == averageWateringPeriod 업데이트 안함!!
        // postponeDate를 업데이트함
        plantRepository.save(plant.updatePostponeDate());

        // 미뤘어요 메시지 X
        // 새로운 watering code를 보내서 맨 뒤로 보냄
        PlantDto.PlantResponse plantResponse = PlantDto.PlantResponse.from(plant);

        // waitingList에서는 오늘 하루만 없애줌
        return 6;
    }
}
