package xyz.notagardener.todo.service;

import xyz.notagardener.todo.dto.TodoDto;

import java.util.List;

public interface TodoService {
    TodoDto add(TodoDto todoDto, Long gardenerId);

    List<TodoDto> getAll(Long gardenerId);

    TodoDto update(TodoDto todoDto, Long gardenerId);

    TodoDto complete(TodoDto todoDto, Long gardenerId);

    void delete(Long todoId, Long gardenerId);
}
