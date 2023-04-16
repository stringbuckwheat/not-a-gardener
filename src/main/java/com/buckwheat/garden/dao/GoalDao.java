package com.buckwheat.garden.dao;

import com.buckwheat.garden.data.dto.GoalDto;
import com.buckwheat.garden.data.entity.Goal;
import com.buckwheat.garden.data.entity.Member;

import java.util.List;

public interface GoalDao {
    List<Goal> getGoalListBy(int memberNo);
    Goal save(GoalDto.Request goalDto, Member member);
    Goal update(GoalDto.Request goalDto);
    Goal complete(int goalNo);
    void delete(int goalNo);
}
