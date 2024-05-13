package xyz.notagardener.domain.plant.dto.garden;

import xyz.notagardener.domain.watering.dto.WateringMessage;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
public class GardenWateringResponse {
    @Schema(description = "(메인 페이지용) 물 주기 기록 후 수정된 식물 정보")
    private GardenResponse gardenResponse;

    @Schema(description = "물 주기 기록 후 알림 메시지")
    private WateringMessage wateringMsg;
}
