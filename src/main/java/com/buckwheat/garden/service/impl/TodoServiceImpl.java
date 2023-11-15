package com.buckwheat.garden.service.impl;

import com.buckwheat.garden.data.dto.todo.TodoDto;
import com.buckwheat.garden.data.entity.Todo;
import com.buckwheat.garden.repository.command.TodoCommandRepository;
import com.buckwheat.garden.repository.query.TodoQueryRepository;
import com.buckwheat.garden.service.TodoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TodoServiceImpl implements TodoService {
    private final TodoCommandRepository todoCommandRepository;
    private final TodoQueryRepository todoQueryRepository;

    @Override
    public TodoDto add(TodoDto todoDto, Long gardenerId) {
        Todo todo = todoCommandRepository.save(todoDto, gardenerId);

        // dto 변환
        return TodoDto.from(todo);
    }

    @Transactional(readOnly = true)
    public List<TodoDto> getAll(Long gardenerId){
        return todoQueryRepository.findByGardener_GardenerId(gardenerId)
                .stream()
                .map(TodoDto::detailFrom)
                .collect(Collectors.toList());
    }

    @Transactional
    public TodoDto update(TodoDto todoDto){
        Todo todo = todoCommandRepository.update(todoDto);
        return TodoDto.detailFrom(todo);
    }

    @Transactional
    public TodoDto complete(TodoDto todoDto){
        Todo todo = todoCommandRepository.complete(todoDto);
        return TodoDto.detailFrom(todo);
    }

    public void delete(Long todoId){
        todoCommandRepository.delete(todoId);
    }
}
