package com.buckwheat.garden.data.dto.todo;

import com.buckwheat.garden.data.entity.Gardener;
import com.buckwheat.garden.data.entity.Todo;
import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@Builder
@NoArgsConstructor
@Getter
@ToString
public class TodoDto {
    private Long todoId;
    private String task;
    private LocalDate deadline;
    private LocalDate completeDate;

    public TodoDto(String task, LocalDate deadline) {
        this.task = task;
        this.deadline = deadline;
    }

    public Todo toEntityWith(Gardener gardener){
        return Todo.builder()
                .task(task)
                .deadline(deadline)
                .createDate(LocalDate.now())
                .gardener(gardener)
                .build();
    }

    // save response
    public static TodoDto from(Todo todo){
        return TodoDto.builder()
                .todoId(todo.getTodoId())
                .task(todo.getTask())
                .deadline(todo.getDeadline())
                .build();
    }

    // getAll response
    public static TodoDto detailFrom(Todo todo){
        return TodoDto.builder()
                .todoId(todo.getTodoId())
                .task(todo.getTask())
                .deadline(todo.getDeadline())
                .completeDate(todo.getCompleteDate())
                .build();
    }
}
