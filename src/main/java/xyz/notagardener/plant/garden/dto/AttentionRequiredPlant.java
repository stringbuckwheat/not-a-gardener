package xyz.notagardener.plant.garden.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import xyz.notagardener.common.validation.YesOrNoType;
import xyz.notagardener.plant.Plant;
import xyz.notagardener.status.model.Status;

@NoArgsConstructor
@Getter
public class AttentionRequiredPlant {
    private Long plantId;
    private String plantName;

    private Long statusId;
    private YesOrNoType active;

    public AttentionRequiredPlant(Plant plant) {
        this.plantId = plant.getPlantId();
        this.plantName = plant.getName();

        Status status = plant.getStatus();

        this.statusId = status.getStatusId();
        this.active = status.getAttention();
    }
}
