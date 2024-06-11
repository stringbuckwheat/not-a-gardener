package xyz.notagardener.status.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import xyz.notagardener.common.validation.YesOrNoType;
import xyz.notagardener.status.PlantStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

@NoArgsConstructor
@ToString
@Getter
public class PlantStatusResponse {
    private Long statusId;
    private PlantStatusType status;
    private YesOrNoType active;
    private LocalDate recordedDate; // max 값
    private LocalDateTime createDate;

    @QueryProjection
    @Builder
    public PlantStatusResponse(Long statusId, PlantStatusType status, YesOrNoType active, LocalDate recordedDate, LocalDateTime createDate) {
        this.statusId = statusId;
        this.status = status;
        this.active = active;
        this.recordedDate = recordedDate;
        this.createDate = createDate;
    }

    public PlantStatusResponse(PlantStatus status) {
        this.statusId = status.getPlantStatusId();
        this.status = status.getStatus();
        this.active = status.getActive();
        this.recordedDate = status.getRecordedDate();
        this.createDate = status.getCreateDate();
    }
}
