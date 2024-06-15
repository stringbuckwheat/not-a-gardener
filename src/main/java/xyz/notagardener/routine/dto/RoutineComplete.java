package xyz.notagardener.routine.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(description = "루틴 id", example = "1")
    private Long id;

    @Schema(description = "가장 최근 완료한 날", example = "2024-02-01")
    private LocalDate lastCompleteDate;
}
