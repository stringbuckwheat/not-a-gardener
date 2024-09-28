package xyz.notagardener.todo.repository;

import org.springframework.data.repository.Repository;
import xyz.notagardener.todo.model.Todo;

import java.util.List;
import java.util.Optional;

public interface TodoRepository extends Repository<Todo, Long> {
    List<Todo> findByGardener_GardenerId(Long gardenerId);
    Todo save(Todo todo);
    Optional<Todo> findById(Long todoId);
    void deleteById(Long todoId);
    void delete(Todo todo);
}
