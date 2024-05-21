package xyz.notagardener.gardener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GardenerUtils {
    // 정규 표현식 패턴
    private static final String PASSWORD_PATTERN =
            "(?=.*\\d{1,50})(?=.*[~`!@#$%\\^&*()\\-+=]{1,50})(?=.*[a-zA-Z]{2,50}).{8,50}$";

    private static final Pattern pattern = Pattern.compile(PASSWORD_PATTERN);

    // 비밀번호 검증
    public static boolean isPasswordValid(final String password) {
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }
}
