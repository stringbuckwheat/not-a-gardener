package xyz.notagardener.domain.watering.code;

public enum WateringCode {
    LATE_WATERING(-1), // 물주기 놓침
    NO_RECORD(0), // 물주기 정보 부족
    THIRSTY(1), // 물주기
    CHECK(2), // 체크하기(물주기 하루 전)
    RESCHEDULED(3), // 물주기 늘어나는 중
    LEAVE_HER_ALONE(4), // 놔두세요
    WATERED_TODAY(5), // 오늘 물 줌
    YOU_ARE_LAZY(6); // 유저가 물주기를 미룸

    private final int code;

    WateringCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
