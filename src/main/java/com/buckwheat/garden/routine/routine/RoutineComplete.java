package com.buckwheat.garden.routine.routine;

import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@ToString
public class RoutineComplete {
    private Long id;
    private LocalDate lastCompleteDate;
}
