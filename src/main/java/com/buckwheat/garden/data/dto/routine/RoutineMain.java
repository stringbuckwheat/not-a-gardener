package com.buckwheat.garden.data.dto.routine;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@ToString
public class RoutineMain {
    List<RoutineResponse> todoList;
    List<RoutineResponse> notToDoList;
}