package xyz.notagardener.repot.repot.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class RepotAlarm {
    @Schema(description = "식물 id", example = "6")
    private Long plantId;

    @Schema(description = "식물 이름", example = "엄마 온시디움")
    private String name;

    @Schema(description = "초기 관수 주기", example = "7")
    private Integer earlyWateringPeriod;

    @Schema(description = "최근 관수 주기", example = "7")
    private Integer recentWateringPeriod;
}
