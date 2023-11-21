package com.buckwheat.garden.plant.garden;

import com.buckwheat.garden.watering.dto.WateringMessage;
import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
public class GardenWateringResponse {
    private GardenResponse gardenResponse;
    private WateringMessage wateringMsg;
}
