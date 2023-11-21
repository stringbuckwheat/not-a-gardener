package com.buckwheat.garden.todo;

import com.buckwheat.garden.data.entity.Gardener;
import com.buckwheat.garden.data.entity.Todo;
import com.buckwheat.garden.gardener.GardenerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TodoServiceImpl implements TodoService {
    private final TodoRepository todoRepository;
    private final GardenerRepository gardenerRepository;

    @Override
    @Transactional
    public TodoDto add(TodoDto todoDto, Long gardenerId) {
        Gardener gardener = gardenerRepository.getReferenceById(gardenerId);
        Todo todo = todoRepository.save(todoDto.toEntityWith(gardener));

        // dto 변환
        return TodoDto.from(todo);
    }

    @Transactional(readOnly = true)
    public List<TodoDto> getAll(Long gardenerId) {
        return todoRepository.findByGardener_GardenerId(gardenerId)
                .stream()
                .map(TodoDto::detailFrom)
                .collect(Collectors.toList());
    }

    @Transactional
    public TodoDto update(TodoDto todoDto) {
        Todo todo = todoRepository.findById(todoDto.getTodoId()).orElseThrow(NoSuchElementException::new);
        todo.update(todoDto.getTask(), todoDto.getDeadline());

        return TodoDto.detailFrom(todo);
    }

    @Transactional
    public TodoDto complete(TodoDto todoDto) {
        Todo todo = todoRepository.findById(todoDto.getTodoId()).orElseThrow(NoSuchElementException::new);
        todo.complete(todoDto.getCompleteDate());

        return TodoDto.detailFrom(todo);
    }

    public void delete(Long todoId) {
        todoRepository.deleteById(todoId);
    }
}
