package com.buckwheat.garden.dao.impl;

import com.buckwheat.garden.dao.RoutineDao;
import com.buckwheat.garden.data.dto.RoutineDto;
import com.buckwheat.garden.data.entity.Member;
import com.buckwheat.garden.data.entity.Plant;
import com.buckwheat.garden.data.entity.Routine;
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

    @Override
    public List<Routine> getRoutineListByMemberNo(int memberNo){
        return routineRepository.findByMember_MemberNo(memberNo);
    }

    @Override
    public Routine save(RoutineDto.Request routineDto, Member member){
        Plant plant = plantRepository.findById(routineDto.getPlantNo()).orElseThrow(NoSuchElementException::new);
        return routineRepository.save(routineDto.toEntityWith(plant, member));
    }

    @Override
    public Routine update(RoutineDto.Request routineDto){
        Plant plant = plantRepository.findById(routineDto.getPlantNo()).orElseThrow(NoSuchElementException::new);
        Routine prevRoutine = routineRepository.findByRoutineNo(routineDto.getRoutineNo()).orElseThrow(NoSuchElementException::new);

        return routineRepository.save(prevRoutine.update(routineDto.getRoutineContent(), routineDto.getRoutineCycle(), plant));
    }

    @Override
    public Routine complete(RoutineDto.Complete routineDto){
        Routine routine = routineRepository.findByRoutineNo(routineDto.getRoutineNo()).orElseThrow(NoSuchElementException::new);
        return routineRepository.save(routine.complete(routineDto.getLastCompleteDate()));
    }

    @Override
    public void delete(int routineNo){
        routineRepository.deleteById(routineNo);
    }
}
