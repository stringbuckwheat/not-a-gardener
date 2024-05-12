package com.buckwheat.garden.domain.routine.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@ToString
public class RoutineComplete {
    @NotNull
    private Long id;

    @NotNull
    private LocalDate lastCompleteDate;
}
