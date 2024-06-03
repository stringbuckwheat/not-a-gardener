package xyz.notagardener.repot.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import xyz.notagardener.common.validation.NotFuture;
import xyz.notagardener.common.validation.YesOrNo;
import xyz.notagardener.plant.Plant;
import xyz.notagardener.status.PlantStatus;
import xyz.notagardener.status.dto.PlantStatusType;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@ToString
public class RepotRequest {
    private Long repotId;

    @NotNull(message = "요청 데이터를 확인해주세요")
    @Positive(message = "요청 데이터를 확인해주세요")
    private Long plantId;

    @NotNull
    @NotFuture
    private LocalDate repotDate;

    @YesOrNo
    private String haveToInitPeriod;

    public PlantStatus toPlantStatusWith(Plant plant) {
        return PlantStatus.builder()
                .status(PlantStatusType.JUST_REPOTTED.getType())
                .recordedDate(repotDate)
                .plant(plant)
                .build();
    }
}
