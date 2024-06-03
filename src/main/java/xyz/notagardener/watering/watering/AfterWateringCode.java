package xyz.notagardener.watering.watering;

public enum AfterWateringCode {
    SCHEDULE_SHORTEN("SCHEDULE_SHORTEN"),
    NO_CHANGE("NO_CHANGE"),
    SCHEDULE_LENGTHEN("SCHEDULE_LENGTHEN"),
    FIRST_WATERING("FIRST_WATERING"),
    SECOND_WATERING("SECOND_WATERING"),
    INIT_WATERING_PERIOD("INIT_WATERING_PERIOD"),
    NO_RECORD("NO_RECORD"),
    POSSIBLE_HEAVY_DRINKER("POSSIBLE_HEAVY_DRINKER");

    private final String code;

    AfterWateringCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
