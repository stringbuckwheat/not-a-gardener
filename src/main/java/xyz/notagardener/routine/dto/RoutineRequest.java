package xyz.notagardener.domain.routine.dto;

import xyz.notagardener.domain.gardener.Gardener;
import xyz.notagardener.domain.plant.Plant;
import xyz.notagardener.domain.routine.Routine;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@ToString
public class RoutineRequest {
    private Long id;

    @NotBlank
    private String content;

    @NotBlank
    private int cycle;

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
