package xyz.notagardener.domain.plant.dto.garden;

import xyz.notagardener.domain.routine.Routine;
import xyz.notagardener.domain.plant.dto.projection.RawGarden;
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
