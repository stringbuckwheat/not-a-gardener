package com.buckwheat.garden.repository;

import com.buckwheat.garden.data.entity.Goal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GoalRepository extends JpaRepository<Goal, Integer> {
    List<Goal> findByMember_MemberNo(int memberNo);
}
