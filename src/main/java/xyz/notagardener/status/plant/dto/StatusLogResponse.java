package xyz.notagardener.status.plant.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(description = "식물 상태 id", example = "1")
    private Long statusLogId;

    @Schema(description = "식물 상태 내용", example = "ATTENTION_PLEASE")
    private StatusType statusType;

    @Schema(description = "활성화 여부", example = "Y")
    private YesOrNoType active;

    @Schema(description = "기록일 (사용자 입력 값)", example = "2024-06-01")
    private LocalDate recordedDate; // max 값

    @Schema(description = "등록일", example = "2024-06-04")
    private LocalDateTime createDate;

    @Schema(description = "해당 식물 id", example = "2")
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
