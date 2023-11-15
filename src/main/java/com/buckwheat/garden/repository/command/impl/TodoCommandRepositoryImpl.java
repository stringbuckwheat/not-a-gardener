package com.buckwheat.garden.repository.command.impl;

import com.buckwheat.garden.data.dto.todo.TodoDto;
import com.buckwheat.garden.data.entity.Gardener;
import com.buckwheat.garden.data.entity.Todo;
import com.buckwheat.garden.repository.command.TodoCommandRepository;
import com.buckwheat.garden.repository.dao.GardenerDao;
import com.buckwheat.garden.repository.dao.TodoDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.NoSuchElementException;

@Repository
@RequiredArgsConstructor
public class TodoCommandRepositoryImpl implements TodoCommandRepository {
    private final TodoDao todoDao;
    private final GardenerDao gardenerDao;

    @Override
    public Todo save(TodoDto todoDto, Long gardenerId) {
        Gardener gardener = gardenerDao.getReferenceById(gardenerId);
        return todoDao.save(todoDto.toEntityWith(gardener));
    }

    @Override
    public Todo update(TodoDto todoDto) {
        Todo todo = todoDao.findById(todoDto.getTodoId()).orElseThrow(NoSuchElementException::new);
        todo.update(todoDto.getTask(), todoDto.getDeadline());
        return todo;
    }

    @Override
    public Todo complete(TodoDto todoDto) {
        Todo todo = todoDao.findById(todoDto.getTodoId()).orElseThrow(NoSuchElementException::new);
        todo.complete(todoDto.getCompleteDate());
        return todo;
    }

    @Override
    public void delete(Long todoId) {
        todoDao.deleteById(todoId);
    }
}
