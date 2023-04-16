package com.buckwheat.garden.dao.impl;

import com.buckwheat.garden.dao.GoalDao;
import com.buckwheat.garden.data.dto.GoalDto;
import com.buckwheat.garden.data.entity.Goal;
import com.buckwheat.garden.data.entity.Member;
import com.buckwheat.garden.data.entity.Plant;
import com.buckwheat.garden.repository.GoalRepository;
import com.buckwheat.garden.repository.PlantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.NoSuchElementException;

@Repository
@RequiredArgsConstructor
public class GoalDaoImpl implements GoalDao {
    private final GoalRepository goalRepository;
    private final PlantRepository plantRepository;

    @Override
    public List<Goal> getGoalListBy(int memberNo){
        return goalRepository.findByMember_MemberNo(memberNo);
    }

    @Override
    public Goal save(GoalDto.Request goalDto, Member member){
        Plant plant = plantRepository.findById(goalDto.getPlantNo()).orElse(null);
        return goalRepository.save(goalDto.toEntityWith(member, plant));
    }

    @Override
    public Goal update(GoalDto.Request goalDto) {
        Plant plant = plantRepository.findById(goalDto.getPlantNo()).orElseThrow(NoSuchElementException::new);
        Goal prevGoal = goalRepository.findById(goalDto.getGoalNo()).orElseThrow(NoSuchElementException::new);

        return goalRepository.save(prevGoal.update(goalDto.getGoalContent(), plant));
    }

    @Override
    public Goal complete(int goalNo){
        Goal prevGoal = goalRepository.findByGoalNo(goalNo).orElseThrow(NoSuchElementException::new);

        // 들어갈 값 계산
        String complete = prevGoal.getComplete().equals("Y") ? "N" : "Y";

        return goalRepository.save(prevGoal.completeGoal(complete));
    }

    @Override
    public void delete(int goalNo){
        goalRepository.deleteById(goalNo);
    }
}
