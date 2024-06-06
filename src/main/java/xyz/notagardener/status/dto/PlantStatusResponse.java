package xyz.notagardener.status.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import xyz.notagardener.common.validation.YesOrNoType;
import xyz.notagardener.status.PlantStatus;

import java.time.LocalDate;

@NoArgsConstructor
@ToString
@Getter
public class PlantStatusResponse {
    private Long statusId;
    private PlantStatusType status;
    private YesOrNoType active;
    private LocalDate recordedDate; // max 값

    @QueryProjection
    public PlantStatusResponse(Long statusId, PlantStatusType status, YesOrNoType active, LocalDate recordedDate) {
        this.statusId = statusId;
        this.status = status;
        this.active = active;
        this.recordedDate = recordedDate;
    }

    public PlantStatusResponse(PlantStatus status) {
        this.statusId = status.getPlantStatusId();
        this.status = status.getStatus();
        this.active = status.getActive();
        this.recordedDate = status.getRecordedDate();
    }
}
