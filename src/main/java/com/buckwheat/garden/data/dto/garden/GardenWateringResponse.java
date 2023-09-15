package com.buckwheat.garden.data.dto.garden;

import com.buckwheat.garden.data.dto.watering.WateringMessage;
import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
public class GardenWateringResponse {
    private GardenResponse gardenResponse;
    private WateringMessage wateringMsg;
}
