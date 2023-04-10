package com.buckwheat.garden.service.impl;

import com.buckwheat.garden.data.dto.RoutineDto;
import com.buckwheat.garden.data.entity.Member;
import com.buckwheat.garden.data.entity.Plant;
import com.buckwheat.garden.data.entity.Routine;
import com.buckwheat.garden.repository.PlantRepository;
import com.buckwheat.garden.repository.RoutineRepository;
import com.buckwheat.garden.service.RoutineService;
import com.buckwheat.garden.util.RoutineUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class RoutineServiceImpl implements RoutineService {
    private final PlantRepository plantRepository;
    private final RoutineRepository routineRepository;
    private final RoutineUtil routineUtil;

    @Override
    public RoutineDto.Main getRoutineList(int memberNo) {
        List<RoutineDto.Response> toDoList = new ArrayList<>();
        List<RoutineDto.Response> notToDoList = new ArrayList<>();

        LocalDateTime today = LocalDate.now().atStartOfDay();

        // dto로 변환
        for (Routine routine : routineRepository.findByMember_MemberNo(memberNo)) {
            // 오늘 해야 하는 루틴인지 계산
            String hasTodoToday = routineUtil.hasToDoToday(routine, today);

            // 오늘 이 루틴을 완료했는지
            String isCompleted = "";

            if(hasTodoToday.equals("Y")){
                isCompleted = routineUtil.isCompleted(routine.getLastCompleteDate(), today);
                toDoList.add(RoutineDto.Response.from(routine, hasTodoToday, isCompleted));
                continue;
            }

            notToDoList.add(RoutineDto.Response.from(routine, hasTodoToday, isCompleted));
        }

        return new RoutineDto.Main(toDoList, notToDoList);
    }

    @Override
    public RoutineDto.Response addRoutine(Member member, RoutineDto.Request routineDto) {
        Plant plant = plantRepository.findById(routineDto.getPlantNo()).orElseThrow(NoSuchElementException::new);
        Routine routine = routineRepository.save(routineDto.toEntityWith(plant, member));

        return RoutineDto.Response.from(routine, plant, "Y", "N");
    }

    @Override
    public RoutineDto.Response modifyRoutine(RoutineDto.Request routineDto) {
        // content, plant
        Plant plant = plantRepository.findById(routineDto.getRoutineNo()).orElseThrow(NoSuchElementException::new);
        Routine prevRoutine = routineRepository.findByRoutineNo(routineDto.getRoutineNo()).orElseThrow(NoSuchElementException::new);

        Routine routine = routineRepository.save(prevRoutine.update(routineDto.getRoutineContent(), routineDto.getRoutineCycle(), plant));

        LocalDateTime today = LocalDate.now().atStartOfDay();
        String hasTodoToday = routineUtil.hasToDoToday(routine, today);
        String isCompleted = routineUtil.isCompleted(routine.getLastCompleteDate(), today);

        return RoutineDto.Response.from(routine, plant, hasTodoToday, isCompleted);
    }

    @Override
    public RoutineDto.Response complete(RoutineDto.Complete routineDto) {
        Routine routine = routineRepository.findByRoutineNo(routineDto.getRoutineNo()).orElseThrow(NoSuchElementException::new);
        routineRepository.save(routine.complete(routineDto.getLastCompleteDate()));
        String isCompleted = routineDto.getLastCompleteDate() != null ? "Y" : "N";

        return RoutineDto.Response.from(routine, "Y", isCompleted);
    }

    @Override
    public void deleteRoutine(int routineNo) {
        routineRepository.deleteById(routineNo);
    }
}
