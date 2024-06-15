package xyz.notagardener.watering.watering.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import xyz.notagardener.chemical.Chemical;
import xyz.notagardener.common.validation.NotFuture;
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
    @Schema(description = "물주기 id", example = "1")
    private Long id;

    @NotNull(message = "식물 정보는 비워둘 수 없어요")
    @Schema(description = "해당 식물 id", example = "2")
    private Long plantId;

    @Schema(description = "약품 id", example = "3")
    private Long chemicalId;

    @NotNull(message = "물 준 날짜는 비워둘 수 없어요")
    @NotFuture(message = "미래에 물을 줄 수 없어요")
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
