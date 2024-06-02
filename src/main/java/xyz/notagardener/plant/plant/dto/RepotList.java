package xyz.notagardener.plant.plant.dto;

import com.querydsl.core.annotations.QueryProjection;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;

@NoArgsConstructor
@Getter
@ToString
public class RepotList {
    @Schema(description = "분갈이 id", example = "1")
    private Long repotId;

    @Schema(description = "분갈이 날짜", example = "2024-05-31")
    private LocalDate repotDate;

    @Schema(description = "물주기 간격 초기화 여부", example = "Y")
    private String initPeriod;

    @Schema(description = "식물 id", example = "2")
    private Long plantId;

    @Schema(description = "식물 이름", example = "벌레잡이 제비꽃")
    private String plantName;

    @Schema(description = "식물 상태 id", example = "1")
    private Long plantStatusId;

    @Schema(description = "식물 상태", example = "요주의 식물")
    private String status;

    @QueryProjection
    public RepotList(Long repotId, LocalDate repotDate, String initPeriod, Long plantId, String plantName, Long plantStatusId, String status) {
        this.repotId = repotId;
        this.repotDate = repotDate;
        this.initPeriod = initPeriod;
        this.plantId = plantId;
        this.plantName = plantName;
        this.plantStatusId = plantStatusId;
        this.status = status;
    }
}
