package xyz.notagardener.watering.watering.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public class WateringMessage {
    @Schema(description = "물 주기 기록 후 알림 메시지", example = "NO_RECORD")
    private String afterWateringCode;

    @Schema(description = "최근 물 주기 간격", example = "5")
    private int recentWateringPeriod;
}
