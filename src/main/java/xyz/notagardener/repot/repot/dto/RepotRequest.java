package xyz.notagardener.repot.repot.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import xyz.notagardener.common.validation.NotFuture;
import xyz.notagardener.common.validation.YesOrNo;
import xyz.notagardener.common.validation.YesOrNoType;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@ToString
public class RepotRequest {
    @Schema(description = "분갈이 id", example = "6")
    private Long repotId;

    @NotNull(message = "요청 데이터를 확인해주세요")
    @Positive(message = "요청 데이터를 확인해주세요")
    @Schema(description = "식물 id", example = "6")
    private Long plantId;

    @NotNull
    @NotFuture
    @Schema(description = "분갈이 날짜", example = "2024-06-19")
    private LocalDate repotDate;

    @YesOrNo
    @Schema(description = "물 주기 간격 초기화 여부", example = "Y")
    private YesOrNoType haveToInitPeriod;

    @YesOrNo
    @Schema(description = "활성화 여부", example = "Y")
    private YesOrNoType active;
}
