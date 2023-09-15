package com.buckwheat.garden.service.impl;

import com.buckwheat.garden.dao.RoutineDao;
import com.buckwheat.garden.data.dto.routine.RoutineComplete;
import com.buckwheat.garden.data.dto.routine.RoutineMain;
import com.buckwheat.garden.data.dto.routine.RoutineRequest;
import com.buckwheat.garden.data.dto.routine.RoutineResponse;
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
    public RoutineMain getAll(Long gardenerId) {
        List<RoutineResponse> toDoList = new ArrayList<>();
        List<RoutineResponse> notToDoList = new ArrayList<>();

        // dto로 변환
        for (Routine routine : routineDao.getRoutinesByGardenerId(gardenerId)) {
            RoutineResponse r = RoutineResponse.from(routine);

            if(r.getHasToDoToday().equals("Y")){
                toDoList.add(r);
                continue;
            }

            notToDoList.add(r);
        }

        return new RoutineMain(toDoList, notToDoList);
    }

    @Override
    public RoutineResponse add(Long gardenerId, RoutineRequest routineRequest) {
        return RoutineResponse.from(routineDao.save(gardenerId, routineRequest));
    }

    @Override
    public RoutineResponse modify(RoutineRequest routineDto) {
        return RoutineResponse.from(routineDao.update(routineDto));
    }

    @Override
    @Transactional
    public RoutineResponse complete(RoutineComplete routineDto) {
        return RoutineResponse.from(routineDao.complete(routineDto));
    }

    @Override
    public void delete(Long id) {
        routineDao.deleteBy(id);
    }
}
