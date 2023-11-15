package com.buckwheat.garden.service;

import com.buckwheat.garden.data.dto.todo.TodoDto;

import java.util.List;

public interface TodoService {
    TodoDto add(TodoDto todoDto, Long gardenerId);

    List<TodoDto> getAll(Long gardenerId);

    TodoDto update(TodoDto todoDto);

    TodoDto complete(TodoDto todoDto);

    void delete(Long todoId);
}
