package com.buckwheat.garden.service.impl;

import com.buckwheat.garden.data.dto.GoalDto;
import com.buckwheat.garden.data.entity.Goal;
import com.buckwheat.garden.data.entity.Member;
import com.buckwheat.garden.data.entity.Plant;
import com.buckwheat.garden.repository.GoalRepository;
import com.buckwheat.garden.repository.MemberRepository;
import com.buckwheat.garden.repository.PlantRepository;
import com.buckwheat.garden.service.GoalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Slf4j
@RequiredArgsConstructor
public class GoalServiceImpl implements GoalService {
    private final GoalRepository goalRepository;
    private final PlantRepository plantRepository;
    private final MemberRepository memberRepository;

    @Override
    public List<GoalDto.Response> getGoalList(int memberNo) {
        List<GoalDto.Response> goalList = new ArrayList<>();

        for(Goal goal : goalRepository.findByMember_MemberNo(memberNo)){
            goalList.add(GoalDto.Response.from(goal));
        }

        return goalList;
    }

    @Override
    public GoalDto.Response addGoal(GoalDto.Request goalDto, Member member) {
        // null 허용
        Plant plant = plantRepository.findById(goalDto.getPlantNo()).orElse(null);
        Goal goal = goalRepository.save(goalDto.toEntityWith(member, plant));

        return GoalDto.Response.from(goal, plant);
    }

    @Override
    public GoalDto.Response modifyGoal(GoalDto.Request goalDto) {
        Plant plant = plantRepository.findById(goalDto.getPlantNo()).orElseThrow(NoSuchElementException::new);
        Goal prevGoal = goalRepository.findById(goalDto.getGoalNo()).orElseThrow(NoSuchElementException::new);

        Goal goal = goalRepository.save(prevGoal.update(goalDto.getGoalContent(), plant));

        return GoalDto.Response.from(goal, plant);
    }

    @Override
    public GoalDto.Response completeGoal(int goalNo) {
        Goal prevGoal = goalRepository.findByGoalNo(goalNo).orElseThrow(NoSuchElementException::new);

        // 들어갈 값 계산
        String complete = prevGoal.getComplete().equals("Y") ? "N" : "Y";

        Goal goal = goalRepository.save(prevGoal.completeGoal(complete));

        return GoalDto.Response.from(goal);
    }

    @Override
    public void deleteGoal(int goalNo) {
        goalRepository.deleteById(goalNo);
    }
}
