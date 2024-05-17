package xyz.notagardener.routine.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@ToString
public class RoutineMain {
    @Schema(description = "해야할 루틴 목록")
    List<RoutineResponse> todoList;

    @Schema(description = "완료했거나 오늘 하지 않아도 되는 일 목록")
    List<RoutineResponse> notToDoList;
}