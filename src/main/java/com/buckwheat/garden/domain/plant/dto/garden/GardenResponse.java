package com.buckwheat.garden.domain.plant.dto.garden;

import com.buckwheat.garden.domain.plant.dto.plant.PlantResponse;
import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
public class GardenResponse {
    private PlantResponse plant;
    private GardenDetail gardenDetail;
}
