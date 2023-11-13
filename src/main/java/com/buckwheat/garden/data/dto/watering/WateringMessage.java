package com.buckwheat.garden.data.dto.watering;

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

    private int afterWateringCode;
    private int recentWateringPeriod;
}
