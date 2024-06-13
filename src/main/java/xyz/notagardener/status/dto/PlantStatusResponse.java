package xyz.notagardener.status.dto;

import lombok.*;
import xyz.notagardener.common.validation.YesOrNoType;
import xyz.notagardener.status.model.Status;

@NoArgsConstructor
@ToString
@Getter
@AllArgsConstructor
@Builder
public class PlantStatusResponse {
    private Long statusId;
    private YesOrNoType attention;
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
