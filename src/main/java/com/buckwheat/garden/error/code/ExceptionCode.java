package com.buckwheat.garden.error.code;

import lombok.Getter;

import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
public enum ExceptionCode {
    ACCESS_TOKEN_EXPIRED("B001", "액세스 토큰 만료", "액세스 토큰 만료"),
    REFRESH_TOKEN_EXPIRED("B002", "리프레쉬 토큰 만료", "로그인 시간이 만료되었습니다"),
    WRONG_ACCOUNT("B003", "아이디/비밀번호 오류", "아이디 또는 비밀번호를 다시 확인해주세요."),
    NO_ACCOUNT("B004", "계정 정보 없음", "해당 유저를 찾을 수 없어요"),
    ALREADY_WATERED("B005", "이미 물 준 날짜", "이 날짜엔 이미 물을 줬어요"),
    NO_SUCH_ITEM("B006", "해당 아이템 없음", "해당 아이템을 찾을 수 없어요"),
    NO_ACCOUNT_FOR_EMAIL("B007", "해당 이메일의 가입 계정 없음", "해당 이메일로 가입한 회원이 없어요"),
    WRONG_PASSWORD("B008", "비밀번호 오류", "비밀번호를 확인해주세요"),
    NO_TOKEN_IN_REDIS("B009", "레디스에 사용자 없음", "로그인 시간이 만료되었습니다. 다시 로그인해주세요"),
    INVALID_REFRESH_TOKEN("B011", "유효하지 않은 리프레쉬 토큰", "비정상적인 움직임이 발생했어요. 다시 로그인해주세요."),
    INVALID_JWT_TOKEN("B010", "유효하지 않은 액세스 토큰", "접근 권한이 없어요. 다시 로그인해주세요"),
    CANNOT_LOGIN("B012", "로그인 할 수 없음", "로그인 할 수 없음");

    private static final Map<String, String> CODE_MAP = Collections.unmodifiableMap(
            Stream.of(values()).collect(Collectors.toMap(ExceptionCode::getCode, ExceptionCode::name))
    );

    private String code;
    private String title;
    private String message;

    ExceptionCode(String code, String title, String message){
        this.code = code;
        this.title = title;
        this.message = message;
    }

    public static ExceptionCode of(String code){
        return ExceptionCode.valueOf(CODE_MAP.get(code));
    }
}
