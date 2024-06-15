package xyz.notagardener.plant.plant.dto;

import com.querydsl.core.annotations.QueryProjection;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import xyz.notagardener.plant.Plant;

@NoArgsConstructor
@Getter
public class PlantBasic {
    @Schema(description = "식물 id", example = "5")
    private Long id;

    @Schema(description = "식물 이름", example = "프릴 거북 알로카시아")
    private String name;

    @QueryProjection
    public PlantBasic(Plant plant) {
        this.id = plant.getPlantId();
        this.name = plant.getName();
    }
}
