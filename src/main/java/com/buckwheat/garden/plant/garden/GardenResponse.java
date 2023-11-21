package com.buckwheat.garden.plant.garden;

import com.buckwheat.garden.plant.plant.PlantResponse;
import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
public class GardenResponse {
    private PlantResponse plant;
    private GardenDetail gardenDetail;
}
