package xyz.notagardener.domain.watering.dto;

import xyz.notagardener.domain.plant.dto.plant.PlantResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@AllArgsConstructor
@Builder
@Getter
@ToString
public class PlantWateringResponse {
    @Schema(description = "식물 정보")
    private PlantResponse plant;

    @Schema(description = "물 주기 기록")
    private List<WateringForOnePlant> waterings;

    @Schema(description = "추가한 물주기에 대한 알림 메시지")
    private WateringMessage wateringMsg;

    public static PlantWateringResponse from(WateringMessage wateringMsg, List<WateringForOnePlant> waterings) {
        return PlantWateringResponse.builder()
                .wateringMsg(wateringMsg)
                .waterings(waterings)
                .build();
    }

    public static PlantWateringResponse from(PlantResponse plant, WateringMessage wateringMsg, List<WateringForOnePlant> waterings) {
        return PlantWateringResponse.builder()
                .plant(plant)
                .wateringMsg(wateringMsg)
                .waterings(waterings)
                .build();
    }
}