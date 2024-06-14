package xyz.notagardener.status.common.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StatusType {
    ATTENTION_PLEASE("ATTENTION_PLEASE"),
    HEAVY_DRINKER("HEAVY_DRINKER");

    public final String type;

    // 위 enum 중에 존재하는지 검사
    public static boolean isValid(String type) {
        for(StatusType statusType : values()) {
            if(statusType.getType().equals(type)) {
                return true;
            }
        }

        return false;
    }

    public static boolean isValid(StatusType type) {
        for(StatusType statusType : values()) {
            if(statusType.equals(type)) {
                return true;
            }
        }

        return false;
    }
}
