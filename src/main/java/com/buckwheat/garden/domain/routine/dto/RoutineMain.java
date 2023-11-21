package com.buckwheat.garden.domain.routine.dto;

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