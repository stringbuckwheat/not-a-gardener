package xyz.notagardener.todo.dto;

import jakarta.validation.constraints.Size;
import xyz.notagardener.gardener.Gardener;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import xyz.notagardener.todo.Todo;

import java.time.LocalDate;

@AllArgsConstructor
@Builder
@NoArgsConstructor
@Getter
@ToString
public class TodoDto {
    @Schema(description = "할일 id", example = "1")
    private Long todoId;

    @NotBlank(message = "할 일 내용은 비워둘 수 없어요")
    @Size(min = 1, max = 100)
    @Schema(description = "할일 내용", example = "온시디움 분갈이")
    private String task;

    @Schema(description = "마감 기한", example = "2024-02-07")
    private LocalDate deadline;

    @Schema(description = "완료 일자")
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
