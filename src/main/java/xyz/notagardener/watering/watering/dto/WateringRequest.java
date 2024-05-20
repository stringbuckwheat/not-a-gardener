package xyz.notagardener.watering.watering.dto;

import lombok.*;
import xyz.notagardener.chemical.Chemical;
import xyz.notagardener.plant.Plant;
import xyz.notagardener.watering.Watering;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@ToString
@AllArgsConstructor
@Builder
public class WateringRequest {
    private Long id;

    @NotNull
    private Long plantId;

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
