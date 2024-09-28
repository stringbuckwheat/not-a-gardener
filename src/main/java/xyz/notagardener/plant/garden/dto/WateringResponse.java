package xyz.notagardener.plant.garden.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.ToString;
import xyz.notagardener.chemical.model.Chemical;
import xyz.notagardener.watering.model.Watering;
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
    @Schema(description = "물주기 id", example = "25")
    private Long id;

    @Schema(description = "식물 이름", example = "한란")
    private String plantName;

    @Schema(description = "약품 이름", example = "하이포넥스")
    private String chemicalName;

    @Schema(description = "물 준 날짜", example = "2024-06-15")
    private LocalDate wateringDate;

    @Schema(description = "물 주기 기록 후 알림")
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
