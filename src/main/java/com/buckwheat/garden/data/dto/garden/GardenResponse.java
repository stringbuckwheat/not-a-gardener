package com.buckwheat.garden.data.dto.garden;

import com.buckwheat.garden.data.dto.plant.PlantResponse;
import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
public class GardenResponse {
    private PlantResponse plant;
    private GardenDetail gardenDetail;
}
