package com.buckwheat.garden.data.dto.watering;

import com.buckwheat.garden.data.entity.Watering;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;

@AllArgsConstructor
@Builder
@Getter
@ToString
public class WateringResponseInChemical {
    private Long id;
    private Long plantId;
    private String plantName;
    private Long placeId;
    private String placeName;
    private LocalDate wateringDate;

    public static WateringResponseInChemical from(Watering watering) {
        return WateringResponseInChemical.builder()
                .id(watering.getWateringId())
                .plantId(watering.getPlant().getPlantId())
                .plantName(watering.getPlant().getName())
                .placeId(watering.getPlant().getPlace().getPlaceId())
                .placeName(watering.getPlant().getPlace().getName())
                .wateringDate(watering.getWateringDate())
                .build();
    }
}