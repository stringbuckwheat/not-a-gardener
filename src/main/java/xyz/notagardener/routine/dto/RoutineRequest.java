package xyz.notagardener.routine.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import xyz.notagardener.gardener.Gardener;
import xyz.notagardener.plant.Plant;
import xyz.notagardener.routine.Routine;

import java.time.LocalDate;

@Getter
@ToString
@Builder
public class RoutineRequest {
    private Long id;

    @NotBlank
    private String content;

    @NotBlank
    private int cycle;

    private Long plantId;

    public boolean isValidForSave() {
        return content != null
                && content.length() > 0
                && content.length() <= 50
                && cycle > 0
                && plantId != null
                && plantId > 0;
    }

    public boolean isValidForUpdate() {
        return isValidForSave() && id != null && id > 0;
    }

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
