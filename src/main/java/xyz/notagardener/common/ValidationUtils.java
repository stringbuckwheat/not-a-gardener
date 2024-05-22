package xyz.notagardener.common;

import xyz.notagardener.place.dto.PlaceDto;
import xyz.notagardener.place.dto.PlaceType;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidationUtils {
    // 정규 표현식 패턴
    private static final String PASSWORD_PATTERN =
            "(?=.*\\d{1,50})(?=.*[~`!@#$%\\^&*()\\-+=]{1,50})(?=.*[a-zA-Z]{2,50}).{8,50}$";

    private static final Pattern pattern = Pattern.compile(PASSWORD_PATTERN);

    // 비밀번호 검증
    public static boolean isPasswordValid(final String password) {
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

    public static boolean isYesOrNo(final String field) {
        return "Y".equals(field) || "N".equals(field);
    }

    public static boolean isValidForSavePlace(final String option, final String artificialLight) {
        return PlaceType.isValid(option) && isYesOrNo(artificialLight);
    }

    public static boolean isValidForUpdatePlace(final PlaceDto placeDto) {
        return isValidForSavePlace(placeDto.getOption(), placeDto.getArtificialLight())
                && placeDto.getId() != null && placeDto.getId() > 0;
    }
}
