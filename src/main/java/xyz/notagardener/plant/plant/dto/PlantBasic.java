package xyz.notagardener.plant.plant.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;
import xyz.notagardener.plant.Plant;

@NoArgsConstructor
@Getter
public class PlantBasic {
    private Long id;
    private String name;

    @QueryProjection
    public PlantBasic(Plant plant) {
        this.id = plant.getPlantId();
        this.name = plant.getName();
    }
}
