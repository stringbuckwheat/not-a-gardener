package xyz.notagardener.routine.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@ToString
@AllArgsConstructor
public class RoutineComplete {
    @NotNull
    private Long id;

    @NotNull
    private LocalDate lastCompleteDate;
}
