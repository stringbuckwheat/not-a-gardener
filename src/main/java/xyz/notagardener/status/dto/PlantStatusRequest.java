package xyz.notagardener.status.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import xyz.notagardener.common.validation.NotFuture;
import xyz.notagardener.common.validation.PlantStatusConstraints;
import xyz.notagardener.common.validation.YesOrNoType;
import xyz.notagardener.plant.Plant;
import xyz.notagardener.status.PlantStatus;

import java.time.LocalDate;

@NoArgsConstructor
@Getter
@ToString
public class PlantStatusRequest {
    private Long plantStatusId;

    @NotNull(message = "식물 정보를 확인해주세요")
    @Positive(message = "식물 정보를 확인해주세요")
    private Long plantId;

    @PlantStatusConstraints
    private PlantStatusType status;

    @NotNull(message = "식물 상태 기록일은 비워둘 수 없어요")
    @NotFuture(message = "식물 상태 기록일은 미래가 될 수 없어요")
    private LocalDate recordedDate;

    private YesOrNoType active;

    public PlantStatus toEntityWith(Plant plant) {
        return PlantStatus.builder()
                .status(status)
                .recordedDate(recordedDate)
                .plant(plant)
                .active(active)
                .build();
    }
}
