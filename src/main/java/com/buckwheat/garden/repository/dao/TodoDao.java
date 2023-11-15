package com.buckwheat.garden.repository.dao;

import com.buckwheat.garden.data.entity.Todo;
import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface TodoDao extends Repository<Todo, Long> {
    Todo save(Todo todo);
    Optional<Todo> findById(Long todoId);
    void deleteById(Long todoId);
}
