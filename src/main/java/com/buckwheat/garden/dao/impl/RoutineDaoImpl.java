package com.buckwheat.garden.dao.impl;

import com.buckwheat.garden.dao.RoutineDao;
import com.buckwheat.garden.data.dto.RoutineDto;
import com.buckwheat.garden.data.entity.Member;
import com.buckwheat.garden.data.entity.Plant;
import com.buckwheat.garden.data.entity.Routine;
import com.buckwheat.garden.repository.MemberRepository;
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
    private final MemberRepository memberRepository;

    @Override
    public List<Routine> getRoutinesByMemberId(Long memberId){
        return routineRepository.findByMember_MemberId(memberId);
    }

    @Override
    public Routine save(Long memberId, RoutineDto.Request routineRequest){
        Member member = memberRepository.findById(memberId).orElseThrow(NoSuchElementException::new);
        Plant plant = plantRepository.findById(routineRequest.getPlantId()).orElseThrow(NoSuchElementException::new);
        return routineRepository.save(routineRequest.toEntityWith(plant, member));
    }

    @Override
    public Routine update(RoutineDto.Request routineRequest){
        Plant plant = plantRepository.findById(routineRequest.getPlantId()).orElseThrow(NoSuchElementException::new);
        Routine prevRoutine = routineRepository.findByRoutineId(routineRequest.getId()).orElseThrow(NoSuchElementException::new);

        return routineRepository.save(prevRoutine.update(routineRequest.getContent(), routineRequest.getCycle(), plant));
    }

    @Override
    public Routine complete(RoutineDto.Complete routineComplete){
        Routine routine = routineRepository.findByRoutineId(routineComplete.getId()).orElseThrow(NoSuchElementException::new);
        return routineRepository.save(routine.complete(routineComplete.getLastCompleteDate()));
    }

    @Override
    public void deleteBy(Long id){
        routineRepository.deleteById(id);
    }
}
