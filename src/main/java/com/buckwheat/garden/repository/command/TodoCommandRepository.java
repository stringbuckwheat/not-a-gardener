package com.buckwheat.garden.repository.command;

import com.buckwheat.garden.data.dto.todo.TodoDto;
import com.buckwheat.garden.data.entity.Todo;

public interface TodoCommandRepository {
    Todo save(TodoDto todoDto, Long gardenerId);
    Todo update(TodoDto todoDto);
    Todo complete(TodoDto todoDto);
    void delete(Long todoId);
}
