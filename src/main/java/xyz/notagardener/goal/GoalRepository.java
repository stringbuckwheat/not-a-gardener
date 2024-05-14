package xyz.notagardener.goal;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

public interface GoalRepository extends Repository<Goal, Long> {
    @EntityGraph(attributePaths = {"plant", "gardener"}, type = EntityGraph.EntityGraphType.FETCH)
    Optional<Goal> findByGoalId(Long goalId);

    Optional<Goal> findById(Long goalId);
    Optional<Goal> findByGoalIdAndGardener_GardenerId(Long goalId, Long gardenerId);
    List<Goal> findByGardener_GardenerId(Long gardenerId);

    Goal save(Goal goal);

    void deleteById(Long goalId);
    void deleteByGoalIdAndGardener_gardenerId(Long id, Long gardenerId);

    void delete(Goal goal);
}
