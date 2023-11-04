package com.buckwheat.garden.data.dto.garden;

import com.buckwheat.garden.data.entity.Routine;
import com.buckwheat.garden.data.projection.RawGarden;
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
