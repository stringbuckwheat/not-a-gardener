package com.buckwheat.garden.watering.dto;

import com.buckwheat.garden.data.entity.Chemical;
import com.buckwheat.garden.data.entity.Watering;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;

@AllArgsConstructor
@Builder
@Getter // (InvalidDefinitionException - No serializer found for class)
@ToString
public class WateringForOnePlant {
    private Long id;
    private Long chemicalId;
    private String chemicalName;
    private LocalDate wateringDate;
    private int period;

    public static WateringForOnePlant from(Watering watering) {
        Chemical chemical = watering.getChemical();

        return WateringForOnePlant.builder()
                .id(watering.getWateringId())
                .chemicalId(chemical == null ? 0 : chemical.getChemicalId())
                .chemicalName(chemical == null ? "맹물" : chemical.getName())
                .wateringDate(watering.getWateringDate())
                .build();
    }

    public static WateringForOnePlant withWateringPeriodFrom(Watering watering, int wateringPeriod) {
        Chemical chemical = watering.getChemical();

        return WateringForOnePlant.builder()
                .id(watering.getWateringId())
                .chemicalName(chemical == null ? "맹물" : chemical.getName())
                .wateringDate(watering.getWateringDate())
                .period(wateringPeriod)
                .build();
    }
}
