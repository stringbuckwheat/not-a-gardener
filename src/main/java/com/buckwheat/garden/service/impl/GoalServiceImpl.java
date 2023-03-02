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
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Slf4j
@RequiredArgsConstructor
public class GoalServiceImpl implements GoalService {
    private GoalRepository goalRepository;
    private PlantRepository plantRepository;
    private MemberRepository memberRepository;

    @Override
    // @Transactional(readOnly = true)
    public List<GoalDto> getGoalList(int memberNo) {
        List<GoalDto> goalList = new ArrayList<>();

        for(Goal g : goalRepository.findByMember_MemberNo(memberNo)){
            GoalDto goal = GoalDto.builder()
                    .goalNo(g.getGoalNo())
                    .goal(g.getGoal())
                    .complete(g.getComplete())
                    .plantName(g.getPlant().getPlantName())
                    .build();

            goalList.add(goal);
        }

        return goalList;
    }

    @Override
    public GoalDto addGoal(GoalDto goalDto, Member member) {
        Plant plant = plantRepository.findById(goalDto.getPlantNo()).orElse(null);

        // dto -> entity
        Goal goal = goalRepository.save(Goal.builder()
                .goal(goalDto.getGoal())
                .member(member)
                .plant(plant) // TODO null 집어넣어도 되나?
                .build());

        return GoalDto.builder()
                .goalNo(goal.getGoalNo())
                .goal(goal.getGoal())
                .complete(goal.getComplete())
                .plantName(goal.getPlant().getPlantName())
                .build();
    }

    @Override
    public GoalDto modifyGoal(GoalDto goalDto) {
        Plant plant = plantRepository.findById(goalDto.getPlantNo()).orElseThrow(NoSuchElementException::new);
        Goal prevGoal = goalRepository.findById(goalDto.getGoalNo()).orElseThrow(NoSuchElementException::new);

        Goal goal = goalRepository.save(prevGoal.update(goalDto.getGoal(), plant));

        return GoalDto.builder()
                .goalNo(goal.getGoalNo())
                .goal(goal.getGoal())
                .complete(goal.getComplete())
                .plantName(goal.getPlant().getPlantName())
                .build();
    }

    @Override
    public GoalDto completeGoal(int goalNo) {
        Goal prevGoal = goalRepository.findById(goalNo).orElseThrow(NoSuchElementException::new);

        // 들어갈 값 계산
        String complete = "Y";

        if(prevGoal.getComplete().equals("Y")){
            complete = "N";
        }

        Goal goal = goalRepository.save(prevGoal.completeGoal(complete));

        return GoalDto.builder()
                .goalNo(goal.getGoalNo())
                .goal(goal.getGoal())
                .complete(goal.getComplete())
                .plantName(goal.getPlant().getPlantName())
                .build();
    }

    @Override
    public void deleteGoal(int goalNo) {
        goalRepository.deleteById(goalNo);
    }
}
