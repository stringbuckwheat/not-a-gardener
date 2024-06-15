package xyz.notagardener.todo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.notagardener.common.error.code.ExceptionCode;
import xyz.notagardener.common.error.exception.ResourceNotFoundException;
import xyz.notagardener.common.error.exception.UnauthorizedAccessException;
import xyz.notagardener.gardener.model.Gardener;
import xyz.notagardener.gardener.repository.GardenerRepository;
import xyz.notagardener.todo.Todo;
import xyz.notagardener.todo.repository.TodoRepository;
import xyz.notagardener.todo.dto.TodoDto;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TodoServiceImpl implements TodoService {
    private final TodoRepository todoRepository;
    private final GardenerRepository gardenerRepository;

    private Todo getToDoByToDoIdAndGardenerId(Long todoId, Long gardenerId) {
        Todo todo = todoRepository.findById(todoId)
                .orElseThrow(() -> new ResourceNotFoundException(ExceptionCode.NO_SUCH_TODO));

        if (!todo.getGardener().getGardenerId().equals(gardenerId)) {
            throw new UnauthorizedAccessException(ExceptionCode.NOT_YOUR_TODO);
        }

        return todo;
    }

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
        return todoRepository.findByGardener_GardenerId(gardenerId).stream().map(TodoDto::detailFrom).toList();
    }

    @Transactional
    public TodoDto update(TodoDto todoDto, Long gardenerId) {
        Todo todo = getToDoByToDoIdAndGardenerId(todoDto.getTodoId(), gardenerId);
        todo.update(todoDto.getTask(), todoDto.getDeadline());

        return TodoDto.detailFrom(todo);
    }

    @Transactional
    public TodoDto complete(TodoDto todoDto, Long gardenerId) {
        Todo todo = getToDoByToDoIdAndGardenerId(todoDto.getTodoId(), gardenerId);
        todo.complete(todoDto.getCompleteDate());

        return TodoDto.detailFrom(todo);
    }

    public void delete(Long todoId, Long gardenerId) {
        Todo todo = getToDoByToDoIdAndGardenerId(todoId, gardenerId);
        todoRepository.delete(todo);
    }
}
