package xyz.notagardener.todo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import xyz.notagardener.common.error.exception.ResourceNotFoundException;
import xyz.notagardener.common.error.exception.UnauthorizedAccessException;
import xyz.notagardener.gardener.model.Gardener;
import xyz.notagardener.gardener.repository.GardenerRepository;
import xyz.notagardener.todo.dto.TodoDto;
import xyz.notagardener.todo.model.Todo;
import xyz.notagardener.todo.repository.TodoRepository;
import xyz.notagardener.todo.service.TodoServiceImpl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@DisplayName("TODO 컴포넌트 테스트")
class TodoServiceImplTest {
    @Mock
    private TodoRepository todoRepository;

    @Mock
    private GardenerRepository gardenerRepository;

    @InjectMocks
    private TodoServiceImpl todoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("TODO 저장: 성공")
    void add_ShouldReturnTodoDto() {
        // Given
        Long gardenerId = 1L;
        TodoDto request = TodoDto.builder().task("TASK").deadline(LocalDate.now().plusDays(30)).build();

        Gardener gardener = Gardener.builder().gardenerId(gardenerId).build();
        Todo todo = Todo.builder().todoId(2L).gardener(gardener).task(request.getTask()).build();

        when(gardenerRepository.getReferenceById(gardenerId)).thenReturn(gardener);
        when(todoRepository.save(any(Todo.class))).thenReturn(todo);

        // When
        TodoDto result = todoService.add(request, gardenerId);

        // Then
        assertEquals(request.getTask(), result.getTask());
        verify(todoRepository, times(1)).save(any(Todo.class));
    }


    @Test
    @DisplayName("한 회원의 전체 todo 목록: 성공")
    void getAll_ShouldReturnListOfTodos() {
        // Given
        Long gardenerId = 1L;

        Gardener gardener = Gardener.builder().gardenerId(gardenerId).build();

        List<Todo> todos = List.of(
                Todo.builder().todoId(2L).gardener(gardener).task("TASK 1").build(),
                Todo.builder().todoId(3L).gardener(gardener).task("TASK 2").build(),
                Todo.builder().todoId(4L).gardener(gardener).task("TASK 3").build()
        );

        when(todoRepository.findByGardener_GardenerId(1L)).thenReturn(todos);

        // When
        List<TodoDto> result = todoService.getAll(1L);

        // Then
        assertEquals(todos.size(), result.size());
        assertEquals(todos.get(0).getTask(), result.get(0).getTask());
        verify(todoRepository, times(1)).findByGardener_GardenerId(gardenerId);
    }

    @Test
    @DisplayName("TODO 수정: 성공")
    void update_ShouldUpdatedTodoDto() {
        // Given
        Long gardenerId = 1L;
        Long todoId = 2L;

        Gardener gardener = Gardener.builder().gardenerId(gardenerId).build();
        Todo todo = Todo.builder().todoId(todoId).gardener(gardener).task("TASK 1").build();
        TodoDto todoDto = TodoDto.builder().todoId(todoId).task("UPDATED TODO").build();

        when(todoRepository.findById(todoId)).thenReturn(Optional.of(todo));

        // When
        TodoDto result = todoService.update(todoDto, gardenerId);

        // Then
        assertEquals(todoDto.getTask(), result.getTask());
        verify(todoRepository, times(1)).findById(todoId);
    }

    @Test
    @DisplayName("TODO 수정: 그런 TODO 없음")
    void update_WhenTodoNotExist_ShouldThrowResourceNotFoundException() {
        // Given
        Long gardenerId = 1L;
        Long todoId = 2L;

        TodoDto todoDto = TodoDto.builder().todoId(todoId).task("UPDATED TODO").build();

        when(todoRepository.findById(todoId)).thenReturn(Optional.empty());

        // When, Then
        Executable executable = () -> todoService.update(todoDto, gardenerId);
        ResourceNotFoundException e = assertThrows(ResourceNotFoundException.class, executable);
        verify(todoRepository, times(1)).findById(todoId);
    }

    @Test
    @DisplayName("TODO 수정: 내 TODO가 아님 - 실패")
    void update_WhenTodoNotMine_ShouldThrowUnauthorizedAccessException() {
        // Given
        Long requesterId = 1L;
        Long todoId = 2L;

        Gardener owner = Gardener.builder().gardenerId(3L).build();
        Todo todo = Todo.builder().todoId(todoId).gardener(owner).task("TASK 1").build();
        TodoDto todoDto = TodoDto.builder().todoId(todoId).task("UPDATED TODO").build();

        when(todoRepository.findById(todoId)).thenReturn(Optional.of(todo));

        // When, Then
        Executable executable = () -> todoService.update(todoDto, requesterId);
        UnauthorizedAccessException e = assertThrows(UnauthorizedAccessException.class, executable);
        verify(todoRepository, times(1)).findById(todoId);
    }

    @Test
    @DisplayName("TODO 완료: 성공")
    void complete_ShouldReturnCompletedTodoDto() {
        // Given
        Long gardenerId = 1L;
        Long todoId = 2L;

        Gardener gardener = Gardener.builder().gardenerId(gardenerId).build();
        Todo todo = Todo.builder().todoId(todoId).gardener(gardener).task("TASK 1").build();
        TodoDto todoDto = TodoDto.builder().todoId(todoId).completeDate(LocalDate.now()).build();

        when(todoRepository.findById(todoId)).thenReturn(Optional.of(todo));

        // When
        TodoDto result = todoService.complete(todoDto, gardenerId);

        // Then
        assertEquals(todoDto.getTodoId(), result.getTodoId());
        assertEquals(LocalDate.now(), result.getCompleteDate());
        verify(todoRepository, times(1)).findById(todoId);
    }

    @Test
    @DisplayName("TODO 완료: 그런 TODO 없음 - 실패")
    void complete_WhenRoutineNotExist_ShouldThrowResourceNotFoundException() {
        // Given
        Long gardenerId = 1L;
        Long todoId = 2L;

        TodoDto todoDto = TodoDto.builder().todoId(todoId).completeDate(LocalDate.now()).build();

        when(todoRepository.findById(todoId)).thenReturn(Optional.empty());

        // When, Then
        Executable executable = () -> todoService.complete(todoDto, gardenerId);
        ResourceNotFoundException e = assertThrows(ResourceNotFoundException.class, executable);
        verify(todoRepository, times(1)).findById(todoId);
    }

    @Test
    @DisplayName("TODO 완료: 내 TODO가 아님 - 실패")
    void complete_WhenRoutineNotMine_ShouldThrowUnauthorizedAccessException() {
        // Given
        Long requesterId = 1L;
        Long todoId = 2L;

        Gardener owner = Gardener.builder().gardenerId(3L).build();
        Todo todo = Todo.builder().todoId(todoId).gardener(owner).task("TASK 1").build();
        TodoDto todoDto = TodoDto.builder().todoId(todoId).task("UPDATED TODO").build();

        when(todoRepository.findById(todoId)).thenReturn(Optional.of(todo));

        // When, Then
        Executable executable = () -> todoService.complete(todoDto, requesterId);
        UnauthorizedAccessException e = assertThrows(UnauthorizedAccessException.class, executable);
        verify(todoRepository, times(1)).findById(todoId);
    }

    @Test
    @DisplayName("TODO 삭제: 성공")
    void delete_Success() {
        // Given
        Long gardenerId = 1L;
        Long todoId = 2L;

        Gardener gardener = Gardener.builder().gardenerId(gardenerId).build();
        Todo todo = Todo.builder().todoId(todoId).gardener(gardener).task("TASK 1").build();

        when(todoRepository.findById(todoId)).thenReturn(Optional.of(todo));

        assertDoesNotThrow(() -> todoService.delete(todoId, gardenerId));
        verify(todoRepository, times(1)).delete(todo);
    }

    @Test
    @DisplayName("TODO 삭제: 그런 TODO 없음 - 실패")
    void delete_WhenTodoNotExist_ShouldThrowResourceNotFoundException() {
        // Given
        Long gardenerId = 1L;
        Long todoId = 2L;

        when(todoRepository.findById(todoId)).thenReturn(Optional.empty());

        // When, Then
        Executable executable = () -> todoService.delete(todoId, gardenerId);
        ResourceNotFoundException e = assertThrows(ResourceNotFoundException.class, executable);
        verify(todoRepository, times(1)).findById(todoId);
    }

    @Test
    @DisplayName("TODO 삭제: 내 TODO 아님 - 실패")
    void delete_WhenTodoNotMine_ShouldThrowUnauthorizedAccessException() {
        // Given
        Long requesterId = 1L;
        Long todoId = 2L;

        Gardener owner = Gardener.builder().gardenerId(3L).build();
        Todo todo = Todo.builder().todoId(todoId).gardener(owner).task("TASK 1").build();
        when(todoRepository.findById(todoId)).thenReturn(Optional.of(todo));

        // When, Then
        Executable executable = () -> todoService.delete(todoId, requesterId);
        UnauthorizedAccessException e = assertThrows(UnauthorizedAccessException.class, executable);
        verify(todoRepository, times(1)).findById(todoId);
    }
}