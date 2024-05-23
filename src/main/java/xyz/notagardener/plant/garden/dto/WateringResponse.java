package xyz.notagardener.plant.garden.dto;

import lombok.ToString;
import xyz.notagardener.chemical.Chemical;
import xyz.notagardener.watering.Watering;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import xyz.notagardener.watering.watering.dto.WateringMessage;

import java.time.LocalDate;

@AllArgsConstructor
@Builder
@Getter
@ToString
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
