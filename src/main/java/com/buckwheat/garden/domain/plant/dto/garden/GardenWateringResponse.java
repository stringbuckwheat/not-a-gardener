package com.buckwheat.garden.domain.plant.dto.garden;

import com.buckwheat.garden.domain.watering.dto.WateringMessage;
import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
public class GardenWateringResponse {
    private GardenResponse gardenResponse;
    private WateringMessage wateringMsg;
}
