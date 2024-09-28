package xyz.notagardener.status.plant.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import xyz.notagardener.common.validation.NotFuture;
import xyz.notagardener.common.validation.PlantStatusConstraints;
import xyz.notagardener.common.validation.YesOrNo;
import xyz.notagardener.common.validation.YesOrNoType;
import xyz.notagardener.plant.model.Plant;
import xyz.notagardener.status.common.model.Status;
import xyz.notagardener.status.common.model.StatusLog;
import xyz.notagardener.status.common.model.StatusType;

import java.time.LocalDate;

@NoArgsConstructor
@Getter
@ToString
public class PlantStatusRequest {
    @Schema(description = "식물 상태 id", example = "1")
    private Long plantStatusId;

    @NotNull(message = "식물 정보를 확인해주세요")
    @Positive(message = "식물 정보를 확인해주세요")
    @Schema(description = "해당 식물 id", example = "2")
    private Long plantId;

    @PlantStatusConstraints
    @Schema(description = "식물 상태 내용", example = "ATTENTION_PLEASE")
    private StatusType statusType;

    @NotNull(message = "식물 상태 기록일은 비워둘 수 없어요")
    @NotFuture(message = "식물 상태 기록일은 미래가 될 수 없어요")
    @Schema(description = "기록일", example = "2024-06-01")
    private LocalDate recordedDate;

    @YesOrNo
    @Schema(description = "활성화 여부", example = "Y")
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
