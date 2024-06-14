package xyz.notagardener.status.plant.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import xyz.notagardener.common.validation.YesOrNoType;
import xyz.notagardener.status.common.model.StatusLog;
import xyz.notagardener.status.common.model.StatusType;

import java.time.LocalDate;
import java.time.LocalDateTime;

@NoArgsConstructor
@ToString
@Getter
public class StatusLogResponse {
    private Long statusLogId;
    private StatusType statusType;
    private YesOrNoType active;
    private LocalDate recordedDate; // max 값
    private LocalDateTime createDate;
    private Long plantId;

    public StatusLogResponse(StatusLog statusLog) {
        this.statusLogId = statusLog.getStatusLogId();
        this.statusType = statusLog.getStatusType();
        this.active = statusLog.getActive();
        this.recordedDate = statusLog.getRecordedDate();
        this.createDate = statusLog.getCreateDate();
        this.plantId = statusLog.getStatus().getPlant().getPlantId();
    }
}
