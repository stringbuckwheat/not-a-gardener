package com.buckwheat.garden.repository;

import com.buckwheat.garden.data.entity.Goal;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface GoalRepository extends Repository<Goal, Long> {
    @EntityGraph(attributePaths = {"plant"}, type = EntityGraph.EntityGraphType.FETCH)
    Optional<Goal> findByGoalId(Long goalId);

    Optional<Goal> findById(Long goalId);
    Goal save(Goal goal);
    void deleteById(Long goalId);
}
