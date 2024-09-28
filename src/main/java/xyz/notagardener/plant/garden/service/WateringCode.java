package xyz.notagardener.plant.garden.service;

public enum WateringCode {
    LATE_WATERING("LATE_WATERING"),
    NOT_ENOUGH_RECORD("NOT_ENOUGH_RECORD"),
    THIRSTY("THIRSTY"),
    CHECK("CHECK"),
    LEAVE_HER_ALONE("LEAVE_HER_ALONE"),
    WATERED_TODAY("WATERED_TODAY"),
    YOU_ARE_LAZY("YOU_ARE_LAZY");

    private final String code;

    WateringCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
