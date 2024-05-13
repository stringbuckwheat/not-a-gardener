package xyz.notagardener.domain.watering.dto;

import xyz.notagardener.domain.chemical.Chemical;
import xyz.notagardener.domain.plant.Plant;
import xyz.notagardener.domain.watering.Watering;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@ToString
public class WateringRequest {
    private Long id;

    @NotNull
    private Long plantId;

    @NotNull
    private Long chemicalId;

    @NotNull
    private LocalDate wateringDate;

    public Watering toEntityWithPlantAndChemical(Plant plant, Chemical chemical) {
        return Watering.builder()
                .plant(plant)
                .chemical(chemical)
                .wateringDate(wateringDate)
                .build();
    }

    public Watering toEntityWithPlant(Plant plant) {
        return Watering.builder()
                .plant(plant)
                .wateringDate(wateringDate)
                .build();
    }
}
