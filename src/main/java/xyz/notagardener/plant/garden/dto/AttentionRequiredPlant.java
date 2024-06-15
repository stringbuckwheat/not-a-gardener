package xyz.notagardener.plant.garden.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import xyz.notagardener.common.validation.YesOrNoType;
import xyz.notagardener.plant.Plant;
import xyz.notagardener.status.common.model.Status;

@NoArgsConstructor
@Getter
public class AttentionRequiredPlant {
    @Schema(description = "식물 id", example = "99")
    private Long plantId;

    @Schema(description = "식물 이름", example = "무늬 몬스테라")
    private String plantName;

    @Schema(description = "식물 상태 id", example = "9")
    private Long statusId;

    @Schema(description = "설정 여부", example = "Y")
    private YesOrNoType active;

    public AttentionRequiredPlant(Plant plant) {
        this.plantId = plant.getPlantId();
        this.plantName = plant.getName();

        Status status = plant.getStatus();

        this.statusId = status.getStatusId();
        this.active = status.getAttention();
    }

    public AttentionRequiredPlant(Status status) {
        Plant plant = status.getPlant();
        this.plantId = plant.getPlantId();
        this.plantName = plant.getName();

        this.statusId = status.getStatusId();
        this.active = status.getAttention();
    }
}
