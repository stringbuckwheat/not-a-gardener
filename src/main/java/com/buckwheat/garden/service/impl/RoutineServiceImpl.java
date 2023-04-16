package com.buckwheat.garden.service.impl;

import com.buckwheat.garden.dao.RoutineDao;
import com.buckwheat.garden.data.dto.RoutineDto;
import com.buckwheat.garden.data.entity.Member;
import com.buckwheat.garden.data.entity.Routine;
import com.buckwheat.garden.service.RoutineService;
import com.buckwheat.garden.util.RoutineUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class RoutineServiceImpl implements RoutineService {
    private final RoutineDao routineDao;
    private final RoutineUtil routineUtil;

    @Override
    public RoutineDto.Main getRoutineList(int memberNo) {
        List<RoutineDto.Response> toDoList = new ArrayList<>();
        List<RoutineDto.Response> notToDoList = new ArrayList<>();

        LocalDateTime today = LocalDate.now().atStartOfDay();

        // dto로 변환
        for (Routine routine : routineDao.getRoutineListByMemberNo(memberNo)) {
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
        Routine routine = routineDao.save(routineDto, member);
        return RoutineDto.Response.from(routine, routine.getPlant(), "Y", "N");
    }

    @Override
    public RoutineDto.Response modifyRoutine(RoutineDto.Request routineDto) {
        Routine routine = routineDao.update(routineDto);

        LocalDateTime today = LocalDate.now().atStartOfDay();
        String hasTodoToday = routineUtil.hasToDoToday(routine, today);
        String isCompleted = routineUtil.isCompleted(routine.getLastCompleteDate(), today);

        return RoutineDto.Response.from(routine, routine.getPlant(), hasTodoToday, isCompleted);
    }

    @Override
    @Transactional
    public RoutineDto.Response complete(RoutineDto.Complete routineDto) {
        Routine routine = routineDao.complete(routineDto);
        String isCompleted = routineDto.getLastCompleteDate() != null ? "Y" : "N";
        return RoutineDto.Response.from(routine, routine.getPlant(),"Y", isCompleted);
    }

    @Override
    public void deleteRoutine(int routineNo) {
        routineDao.delete(routineNo);
    }
}
