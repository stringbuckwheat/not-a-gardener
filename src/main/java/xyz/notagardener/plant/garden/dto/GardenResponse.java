package xyz.notagardener.plant.garden.dto;

import xyz.notagardener.plant.plant.dto.PlantResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
public class GardenResponse {
    @Schema(description = "식물 상세 정보")
    private PlantResponse plant;

    @Schema(description = "식물 계산 정보")
    private GardenDetail gardenDetail;
}
