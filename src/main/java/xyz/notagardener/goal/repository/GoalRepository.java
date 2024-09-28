package xyz.notagardener.goal.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.Repository;
import xyz.notagardener.goal.model.Goal;

import java.util.List;
import java.util.Optional;

public interface GoalRepository extends Repository<Goal, Long> {
    @EntityGraph(attributePaths = {"plant", "gardener"}, type = EntityGraph.EntityGraphType.FETCH)
    Optional<Goal> findByGoalId(Long goalId);

    List<Goal> findByGardener_GardenerId(Long gardenerId);

    Goal save(Goal goal);

    void delete(Goal goal);
}
