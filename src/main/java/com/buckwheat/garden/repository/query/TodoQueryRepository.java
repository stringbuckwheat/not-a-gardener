package com.buckwheat.garden.repository.query;

import com.buckwheat.garden.data.entity.Todo;
import org.springframework.data.repository.Repository;

import java.util.List;

public interface TodoQueryRepository extends Repository<Todo, Long> {
    List<Todo> findByGardener_GardenerId(Long gardenerId);
}
