package com.buckwheat.garden.code;

public enum AfterWateringCode {
    // -1   물주기가 줄어들었어요!
    // 0    물주기 계산에 변동이 없습니다.
    // 1    물주기가 늘어났어요
    // 2    처음으로 물주기를 기록
    // 3    두 번째 물주기 기록

    SCHEDULE_SHORTEN(-1),
    NO_CHANGE(0),
    SCHEDULE_LENGTHEN(1),
    FIRST_WATERING(2),
    SECOND_WATERING(3),
    INIT_WATERING_PERIOD(4);

    private final int code;

    AfterWateringCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
