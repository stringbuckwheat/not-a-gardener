package xyz.notagardener.routine.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import xyz.notagardener.gardener.model.Gardener;
import xyz.notagardener.plant.model.Plant;
import xyz.notagardener.routine.model.Routine;

import java.time.LocalDate;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoutineRequest {
    @Schema(description = "루틴 id", example = "1")
    private Long id;

    @NotBlank(message = "루틴 내용은 비워둘 수 없어요")
    @Schema(description = "루틴 내용", example = "아디안텀 1일 1관수!")
    private String content;

    @NotNull(message = "루틴 반복 주기는 비워둘 수 없어요")
    @Positive(message = "루틴 반복 주기는 양수여야해요")
    @Schema(description = "루틴 반복 주기", example = "1")
    private Integer cycle;

    @Schema(description = "루틴 해당 식물 id", example = "2")
    private Long plantId;

    public Routine toEntityWith(Plant plant, Gardener gardener) {
        return Routine.builder()
                .routineId(id)
                .content(content)
                .cycle(cycle)
                .plant(plant)
                .gardener(gardener)
                .createDate(LocalDate.now())
                .build();
    }
}
