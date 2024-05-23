package xyz.notagardener.place.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum PlaceType {
    INSIDE("실내"),
    BALCONY("베란다"),
    OUTSIDE("야외"),
    OTHER("그 외");

    public final String type;

    // 위 enum 중에 존재하는지 검사
    public static boolean isValid(String type) {
        for(PlaceType placeType : values()) {
            if(placeType.getType().equals(type)) {
                return true;
            }
        }

        return false;
    }
}
