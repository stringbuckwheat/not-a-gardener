package com.buckwheat.garden.repository;

import com.buckwheat.garden.data.entity.Goal;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GoalRepository extends JpaRepository<Goal, Integer> {
    @EntityGraph(attributePaths = {"plant"}, type = EntityGraph.EntityGraphType.FETCH)
    List<Goal> findByMember_MemberNo(int memberNo);

    @EntityGraph(attributePaths = {"plant"}, type = EntityGraph.EntityGraphType.FETCH)
    Optional<Goal> findByGoalNo(int goalNo);
}
