package xyz.notagardener.repot.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import xyz.notagardener.repot.Repot;
import xyz.notagardener.status.PlantStatusResponse;

import java.time.LocalDate;

@NoArgsConstructor
@ToString
@Getter
public class RepotResponse {
    @Schema(description = "분갈이 id", example = "1")
    private Long repotId;

    @Schema(description = "식물 id", example = "2")
    private Long plantId;

    @Schema(description = "분갈이 날짜", example = "2024-05-31")
    private LocalDate repotDate;

    @Schema(description = "변화된 식물 상태 정보")
    private PlantStatusResponse status;

    public RepotResponse(Repot repot, PlantStatusResponse status) {
        this.repotId = repot.getRepotId();
        this.plantId = repot.getPlant().getPlantId();
        this.repotDate = repot.getRepotDate();
        this.status = status;
    }
}
