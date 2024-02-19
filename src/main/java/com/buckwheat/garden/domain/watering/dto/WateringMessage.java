package com.buckwheat.garden.domain.watering.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public class WateringMessage {
    // -1   물주기가 줄어들었어요!
    // 0    물주기 계산에 변동이 없습니다.
    // 1    물주기가 늘어났어요
    // 2    처음으로 물주기를 기록
    // 3    두 번째 물주기 기록

    @Schema(description = "물 주기 기록 후 알림 메시지", example = "-1 // 물 준 간격이 줄어들었어요!")
    private int afterWateringCode;

    @Schema(description = "최근 물 주기 간격", example = "5")
    private int recentWateringPeriod;
}
