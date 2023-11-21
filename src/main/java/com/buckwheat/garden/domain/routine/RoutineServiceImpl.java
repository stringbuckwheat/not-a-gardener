package com.buckwheat.garden.domain.routine;

import com.buckwheat.garden.domain.gardener.Gardener;
import com.buckwheat.garden.domain.plant.Plant;
import com.buckwheat.garden.domain.gardener.repository.GardenerRepository;
import com.buckwheat.garden.domain.plant.repository.PlantRepository;
import com.buckwheat.garden.domain.routine.dto.RoutineComplete;
import com.buckwheat.garden.domain.routine.dto.RoutineMain;
import com.buckwheat.garden.domain.routine.dto.RoutineRequest;
import com.buckwheat.garden.domain.routine.dto.RoutineResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Slf4j
@RequiredArgsConstructor
public class RoutineServiceImpl implements RoutineService {
    private final RoutineRepository routineRepository;
    private final PlantRepository plantRepository;
    private final GardenerRepository gardenerRepository;

    @Override
    @Transactional(readOnly = true)
    public RoutineMain getAll(Long gardenerId) {
        List<RoutineResponse> toDoList = new ArrayList<>();
        List<RoutineResponse> notToDoList = new ArrayList<>();

        // dto로 변환
        for (Routine routine : routineRepository.findByGardener_GardenerId(gardenerId)) {
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
    @Transactional
    public RoutineResponse add(Long gardenerId, RoutineRequest routineRequest) {
        Gardener gardener = gardenerRepository.getReferenceById(gardenerId);
        Plant plant = plantRepository.findById(routineRequest.getPlantId()).orElseThrow(NoSuchElementException::new);
        Routine routine = routineRepository.save(routineRequest.toEntityWith(plant, gardener));

        return RoutineResponse.from(routine);
    }

    @Override
    @Transactional
    public RoutineResponse update(RoutineRequest routineRequest) {
        Plant plant = plantRepository.findById(routineRequest.getPlantId()).orElseThrow(NoSuchElementException::new);
        Routine routine = routineRepository.findByRoutineId(routineRequest.getId()).orElseThrow(NoSuchElementException::new);
        routine.update(routineRequest.getContent(), routineRequest.getCycle(), plant);

        return RoutineResponse.from(routine);
    }

    @Override
    @Transactional
    public RoutineResponse complete(RoutineComplete routineComplete) {
        Routine routine = routineRepository.findByRoutineId(routineComplete.getId()).orElseThrow(NoSuchElementException::new);
        routine.complete(routineComplete.getLastCompleteDate());

        return RoutineResponse.from(routine);
    }

    @Override
    public void delete(Long id) {
        routineRepository.deleteById(id);
    }
}
