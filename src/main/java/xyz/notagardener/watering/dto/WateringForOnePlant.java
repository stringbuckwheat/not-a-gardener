package xyz.notagardener.domain.watering.dto;

import xyz.notagardener.domain.chemical.Chemical;
import xyz.notagardener.domain.watering.Watering;
import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(description = "물주기 id", example = "1")
    private Long id;

    @Schema(description = "약품 id", example = "1")
    private Long chemicalId;

    @Schema(description = "약품 이름", example = "하이포넥스")
    private String chemicalName;

    @Schema(description = "물 준 날짜", example = "2024-02-01")
    private LocalDate wateringDate;

    @Schema(description = "물 준 간격", example = "4")
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
