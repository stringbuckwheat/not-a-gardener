package xyz.notagardener.watering.watering;

public enum AfterWateringCode {
    // -1   물주기가 줄어들었어요!        SCHEDULE_SHORTEN
    // 0    물주기 계산에 변동이 없습니다.   NO_CHANGE
    // 1    물주기가 늘어났어요          SCHEDULE_LENGTHEN
    // 2    처음으로 물주기를 기록        FIRST_WATERING
    // 3    두 번째 물주기 기록         SECOND_WATERING
    // 4    물주기 초기화             INIT_WATERING_PERIOD
    // 5    물주기 정보 없음           NO_RECORD

    SCHEDULE_SHORTEN("SCHEDULE_SHORTEN"),
    NO_CHANGE("NO_CHANGE"),
    SCHEDULE_LENGTHEN("SCHEDULE_LENGTHEN"),
    FIRST_WATERING("FIRST_WATERING"),
    SECOND_WATERING("SECOND_WATERING"),
    INIT_WATERING_PERIOD("INIT_WATERING_PERIOD"),
    NO_RECORD("NO_RECORD");

    private final String code;

    AfterWateringCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
