package com.buckwheat.garden.domain.routine.dto;

import com.buckwheat.garden.domain.gardener.Gardener;
import com.buckwheat.garden.domain.plant.Plant;
import com.buckwheat.garden.domain.routine.Routine;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@ToString
public class RoutineRequest {
    private Long id;
    private String content;
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
