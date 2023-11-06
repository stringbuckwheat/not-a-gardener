package com.buckwheat.garden.repository.query;

import com.buckwheat.garden.data.entity.Goal;
import org.springframework.data.repository.Repository;

import java.util.List;

public interface GoalQueryRepository extends Repository<Goal, Long> {
    List<Goal> findByGardener_GardenerId(Long gardenerId);
}
