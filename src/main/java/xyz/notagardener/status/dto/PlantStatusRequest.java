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
import xyz.notagardener.status.model.Status;
import xyz.notagardener.status.model.StatusLog;
import xyz.notagardener.status.model.StatusType;

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
    private StatusType statusType;

    @NotNull(message = "식물 상태 기록일은 비워둘 수 없어요")
    @NotFuture(message = "식물 상태 기록일은 미래가 될 수 없어요")
    private LocalDate recordedDate;

    private YesOrNoType active;

    public StatusLog toStatusLogWith(Status status) {
        return StatusLog.builder()
                .statusType(statusType)
                .recordedDate(recordedDate)
                .status(status)
                .active(active)
                .build();
    }

    public Status toStatusWith(Plant plant) {
        Status.StatusBuilder builder = Status.builder()
                .plant(plant)
                .attention(YesOrNoType.N)
                .heavyDrinker(YesOrNoType.N);

        if (StatusType.HEAVY_DRINKER.equals(statusType)) {
            builder.heavyDrinker(YesOrNoType.Y);
        } else if (StatusType.ATTENTION_PLEASE.equals(statusType)) {
            builder.attention(YesOrNoType.Y);
        }

        return builder.build();
    }
}
