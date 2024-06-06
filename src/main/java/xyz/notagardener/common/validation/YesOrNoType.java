package xyz.notagardener.common.validation;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum YesOrNoType {
    Y("Y"),
    N("N");

    public final String type;

    public static boolean isValid(YesOrNoType value) {
        return YesOrNoType.Y.equals(value) || YesOrNoType.N.equals(value);
    }
}
