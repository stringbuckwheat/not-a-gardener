package xyz.notagardener.status.plant.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import xyz.notagardener.common.validation.YesOrNoType;
import xyz.notagardener.status.common.model.Status;

@NoArgsConstructor
@ToString
@Getter
@AllArgsConstructor
@Builder
public class PlantStatusResponse {
    @Schema(description = "식물 상태 id", example = "1")
    private Long statusId;

    @Schema(description = "요주의 식물 여부", example = "Y")
    private YesOrNoType attention;

    @Schema(description = "헤비드링커 여부", example = "N")
    private YesOrNoType heavyDrinker;

    public PlantStatusResponse(Status status) {
        if(status == null) {
            this.attention = YesOrNoType.N;
            this.heavyDrinker = YesOrNoType.N;
        } else {
            this.statusId = status.getStatusId();
            this.attention = status.getAttention();
            this.heavyDrinker = status.getHeavyDrinker();
        }
    }
}
