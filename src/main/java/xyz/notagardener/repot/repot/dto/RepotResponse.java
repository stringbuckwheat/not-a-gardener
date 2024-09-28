package xyz.notagardener.repot.repot.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import xyz.notagardener.common.validation.YesOrNoType;
import xyz.notagardener.repot.model.Repot;
import xyz.notagardener.status.plant.dto.PlantStatusResponse;

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

    @Schema(description = "물주기 간격 초기화 여부", example = "Y")
    private YesOrNoType initPeriod;

    @Schema(description = "변화된 식물 상태 정보")
    private PlantStatusResponse status;

    public RepotResponse(Repot repot, PlantStatusResponse status) {
        this.repotId = repot.getRepotId();
        this.plantId = repot.getPlant().getPlantId();
        this.repotDate = repot.getRepotDate();
        this.initPeriod = repot.getInitPeriod();
        this.status = status;
    }

    public RepotResponse(Repot repot) {
        this.repotId = repot.getRepotId();
        this.plantId = repot.getPlant().getPlantId();
        this.repotDate = repot.getRepotDate();
        this.initPeriod = repot.getInitPeriod();
        this.status = new PlantStatusResponse(repot.getPlant().getStatus());
    }
}
