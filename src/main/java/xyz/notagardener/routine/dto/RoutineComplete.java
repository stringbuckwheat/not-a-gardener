package xyz.notagardener.routine.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class RoutineComplete {
    @NotNull(message = "루틴은 비워둘 수 없어요")
    private Long id;

    private LocalDate lastCompleteDate;
}
