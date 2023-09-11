package com.buckwheat.garden.service.impl;

import com.buckwheat.garden.dao.RoutineDao;
import com.buckwheat.garden.data.dto.RoutineDto;
import com.buckwheat.garden.data.entity.Routine;
import com.buckwheat.garden.service.RoutineService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class RoutineServiceImpl implements RoutineService {
    private final RoutineDao routineDao;

    @Override
    public RoutineDto.Main getAll(Long gardenerId) {
        List<RoutineDto.Response> toDoList = new ArrayList<>();
        List<RoutineDto.Response> notToDoList = new ArrayList<>();

        // dto로 변환
        for (Routine routine : routineDao.getRoutinesByGardenerId(gardenerId)) {
            RoutineDto.Response r = RoutineDto.Response.from(routine);

            if(r.getHasToDoToday().equals("Y")){
                toDoList.add(r);
                continue;
            }

            notToDoList.add(r);
        }

        return new RoutineDto.Main(toDoList, notToDoList);
    }

    @Override
    public RoutineDto.Response add(Long gardenerId, RoutineDto.Request routineRequest) {
        return RoutineDto.Response.from(routineDao.save(gardenerId, routineRequest));
    }

    @Override
    public RoutineDto.Response modify(RoutineDto.Request routineDto) {
        return RoutineDto.Response.from(routineDao.update(routineDto));
    }

    @Override
    @Transactional
    public RoutineDto.Response complete(RoutineDto.Complete routineDto) {
        return RoutineDto.Response.from(routineDao.complete(routineDto));
    }

    @Override
    public void delete(Long id) {
        routineDao.deleteBy(id);
    }
}
