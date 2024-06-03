package xyz.notagardener.status.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import xyz.notagardener.status.PlantStatus;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@ToString
public class PlantStatusResponse {
    @Schema(description = "식물 상태 id", example = "1")
    private Long plantStatusId;

    @Schema(description = "식물 상태", example = "요주의 식물")
    private String status;

    @Schema(description = "등록일", example = "2024-05-31")
    private LocalDate recordedDate;

    @Schema(description = "식물 id", example = "2")
    private Long plantId;

    @Schema(description = "식물 이름", example = "벌레잡이 제비꽃")
    private String plantName;

    public PlantStatusResponse(PlantStatus status) {
        this.plantStatusId = status.getPlantStatusId();
        this.status = status.getStatus();
        this.recordedDate = status.getRecordedDate();
        this.plantId = status.getPlant().getPlantId();
        this.plantName = status.getPlant().getName();
    }
}
