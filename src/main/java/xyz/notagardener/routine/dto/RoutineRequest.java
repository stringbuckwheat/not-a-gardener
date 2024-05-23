package xyz.notagardener.routine.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import xyz.notagardener.gardener.Gardener;
import xyz.notagardener.plant.Plant;
import xyz.notagardener.routine.Routine;

import java.time.LocalDate;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoutineRequest {
    private Long id;

    @NotBlank(message = "루틴 내용은 비워둘 수 없어요")
    private String content;

    @NotNull(message = "루틴 반복 주기는 비워둘 수 없어요")
    @Positive(message = "루틴 반복 주기는 양수여야해요")
    private Integer cycle;

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
