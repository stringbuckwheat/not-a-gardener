package com.buckwheat.garden.dao.impl;

import com.buckwheat.garden.dao.RoutineDao;
import com.buckwheat.garden.data.dto.RoutineDto;
import com.buckwheat.garden.data.entity.Gardener;
import com.buckwheat.garden.data.entity.Plant;
import com.buckwheat.garden.data.entity.Routine;
import com.buckwheat.garden.repository.GardenerRepository;
import com.buckwheat.garden.repository.PlantRepository;
import com.buckwheat.garden.repository.RoutineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.NoSuchElementException;

@Repository
@RequiredArgsConstructor
public class RoutineDaoImpl implements RoutineDao {
    private final RoutineRepository routineRepository;
    private final PlantRepository plantRepository;
    private final GardenerRepository gardenerRepository;

    @Override
    public List<Routine> getRoutinesByGardenerId(Long gardenerId){
        return routineRepository.findByGardener_GardenerId(gardenerId);
    }

    @Override
    public Routine save(Long gardenerId, RoutineDto.Request routineRequest){
        Gardener gardener = gardenerRepository.getReferenceById(gardenerId);
        Plant plant = plantRepository.findById(routineRequest.getPlantId()).orElseThrow(NoSuchElementException::new);
        return routineRepository.save(routineRequest.toEntityWith(plant, gardener));
    }

    @Override
    public Routine update(RoutineDto.Request routineRequest){
        Plant plant = plantRepository.findById(routineRequest.getPlantId()).orElseThrow(NoSuchElementException::new);
        Routine routine = routineRepository.findByRoutineId(routineRequest.getId()).orElseThrow(NoSuchElementException::new);
        routine.update(routineRequest.getContent(), routineRequest.getCycle(), plant);

        return routineRepository.save(routine);
    }

    @Override
    public Routine complete(RoutineDto.Complete routineComplete){
        Routine routine = routineRepository.findByRoutineId(routineComplete.getId()).orElseThrow(NoSuchElementException::new);
        routine.complete(routineComplete.getLastCompleteDate());

        return routineRepository.save(routine);
    }

    @Override
    public void deleteBy(Long id){
        routineRepository.deleteById(id);
    }
}
