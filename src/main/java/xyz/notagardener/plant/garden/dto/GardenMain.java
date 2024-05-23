package xyz.notagardener.plant.garden.dto;

import xyz.notagardener.routine.dto.RoutineResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
public class GardenMain {
    @Schema(description = "식물 0개인지", example = "true")
    private boolean hasPlant;

    @Schema(description = "할 일이 있는 식물")
    private List<GardenResponse> todoList;

    @Schema(description = "물 주기 정보가 없는 식물")
    private List<WaitingForWatering> waitingList;

    @Schema(description = "루틴 리스트")
    private List<RoutineResponse> routineList;

    public static GardenMain noPlant() {
        return new GardenMain(false, null, null, null);
    }
}
