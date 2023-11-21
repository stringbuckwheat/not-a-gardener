package com.buckwheat.garden.domain.plant.dto.garden;

import com.buckwheat.garden.domain.routine.Routine;
import com.buckwheat.garden.domain.plant.dto.projection.RawGarden;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class RawGardenMain {
    List<WaitingForWatering> waitingForWatering;
    List<RawGarden> plantsToDo;
    List<Routine> routines;
}
