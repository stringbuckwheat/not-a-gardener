package com.buckwheat.garden.data.dto.garden;

import com.buckwheat.garden.data.dto.routine.RoutineResponse;
import lombok.*;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
public class GardenMain {
    private boolean hasPlant;
    private List<GardenResponse> todoList;
    private List<WaitingForWatering> waitingList;
    private List<RoutineResponse> routineList;
}
