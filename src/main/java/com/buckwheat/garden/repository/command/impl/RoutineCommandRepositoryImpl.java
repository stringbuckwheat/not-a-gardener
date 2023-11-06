package com.buckwheat.garden.repository.command.impl;

import com.buckwheat.garden.repository.command.RoutineCommandRepository;
import com.buckwheat.garden.data.dto.routine.RoutineComplete;
import com.buckwheat.garden.data.dto.routine.RoutineRequest;
import com.buckwheat.garden.data.entity.Gardener;
import com.buckwheat.garden.data.entity.Plant;
import com.buckwheat.garden.data.entity.Routine;
import com.buckwheat.garden.dao.GardenerDao;
import com.buckwheat.garden.dao.PlantDao;
import com.buckwheat.garden.dao.RoutineDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.NoSuchElementException;

@Repository
@RequiredArgsConstructor
public class RoutineCommandRepositoryImpl implements RoutineCommandRepository {
    private final RoutineDao routineDao;
    private final PlantDao plantDao;
    private final GardenerDao gardenerDao;

    @Override
    public Routine save(Long gardenerId, RoutineRequest routineRequest) {
        Gardener gardener = gardenerDao.getReferenceById(gardenerId);
        Plant plant = plantDao.findById(routineRequest.getPlantId()).orElseThrow(NoSuchElementException::new);
        return routineDao.save(routineRequest.toEntityWith(plant, gardener));
    }

    @Override
    public Routine update(RoutineRequest routineRequest) {
        Plant plant = plantDao.findById(routineRequest.getPlantId()).orElseThrow(NoSuchElementException::new);
        Routine routine = routineDao.findByRoutineId(routineRequest.getId()).orElseThrow(NoSuchElementException::new);
        routine.update(routineRequest.getContent(), routineRequest.getCycle(), plant);

        return routineDao.save(routine);
    }

    @Override
    public Routine complete(RoutineComplete routineComplete) {
        Routine routine = routineDao.findByRoutineId(routineComplete.getId()).orElseThrow(NoSuchElementException::new);
        routine.complete(routineComplete.getLastCompleteDate());

        return routineDao.save(routine);
    }

    @Override
    public void deleteBy(Long id) {
        routineDao.deleteById(id);
    }
}
