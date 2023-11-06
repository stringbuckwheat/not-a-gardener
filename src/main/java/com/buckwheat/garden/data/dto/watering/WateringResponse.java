package com.buckwheat.garden.data.dto.watering;

import com.buckwheat.garden.data.entity.Chemical;
import com.buckwheat.garden.data.entity.Watering;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@AllArgsConstructor
@Builder
@Getter
public class WateringResponse {
    private Long id;
    private String plantName;
    private String chemicalName;
    private LocalDate wateringDate;
    private WateringMessage msg;

    public static String getChemicalName(Chemical chemical) {
        if (chemical != null) {
            return chemical.getName();
        }

        return "맹물";
    }

    public static WateringResponse from(Watering watering) {
        return WateringResponse.builder()
                .id(watering.getWateringId())
                .plantName(watering.getPlant().getName())
                .chemicalName(getChemicalName(watering.getChemical()))
                .wateringDate(watering.getWateringDate())
                .build();
    }

    // TODO test 메소드
    public static WateringResponse from(LocalDate latestWateringDate) {
        return WateringResponse.builder().wateringDate(latestWateringDate).build();
    }

    public static WateringResponse withWateringMsgFrom(Watering watering, WateringMessage wateringMsg) {
        return WateringResponse.builder()
                .id(watering.getWateringId())
                .plantName(watering.getPlant().getName())
                .chemicalName(getChemicalName(watering.getChemical()))
                .wateringDate(watering.getWateringDate())
                .msg(wateringMsg)
                .build();
    }
}
