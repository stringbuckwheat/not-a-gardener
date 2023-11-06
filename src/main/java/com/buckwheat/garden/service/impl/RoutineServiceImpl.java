package com.buckwheat.garden.service.impl;

import com.buckwheat.garden.data.dto.routine.RoutineComplete;
import com.buckwheat.garden.data.dto.routine.RoutineMain;
import com.buckwheat.garden.data.dto.routine.RoutineRequest;
import com.buckwheat.garden.data.dto.routine.RoutineResponse;
import com.buckwheat.garden.data.entity.Routine;
import com.buckwheat.garden.repository.command.RoutineCommandRepository;
import com.buckwheat.garden.repository.query.RoutineQueryRepository;
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
    private final RoutineCommandRepository routineCommandRepository;
    private final RoutineQueryRepository routineQueryRepository;

    @Override
    @Transactional(readOnly = true)
    public RoutineMain getAll(Long gardenerId) {
        List<RoutineResponse> toDoList = new ArrayList<>();
        List<RoutineResponse> notToDoList = new ArrayList<>();

        // dto로 변환
        for (Routine routine : routineQueryRepository.findByGardener_GardenerId(gardenerId)) {
            RoutineResponse r = RoutineResponse.from(routine);

            if (r.getHasToDoToday().equals("Y")) {
                toDoList.add(r);
                continue;
            }

            notToDoList.add(r);
        }

        return new RoutineMain(toDoList, notToDoList);
    }

    @Override
    public RoutineResponse add(Long gardenerId, RoutineRequest routineRequest) {
        return RoutineResponse.from(routineCommandRepository.save(gardenerId, routineRequest));
    }

    @Override
    public RoutineResponse update(RoutineRequest routineDto) {
        return RoutineResponse.from(routineCommandRepository.update(routineDto));
    }

    @Override
    @Transactional
    public RoutineResponse complete(RoutineComplete routineDto) {
        return RoutineResponse.from(routineCommandRepository.complete(routineDto));
    }

    @Override
    public void delete(Long id) {
        routineCommandRepository.deleteBy(id);
    }
}
