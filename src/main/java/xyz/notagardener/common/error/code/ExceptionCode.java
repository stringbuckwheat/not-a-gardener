package xyz.notagardener.common.error.code;

import lombok.Getter;

import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
public enum ExceptionCode {
    PLEASE_LOGIN("PLEASE_LOGIN", "인증이 필요한 엔드포인트", "로그인 해주세요"), // B000
    HAS_SAME_USERNAME("HAS_SAME_USERNAME", "이이디 중복", ""),
    ACCESS_TOKEN_EXPIRED("ACCESS_TOKEN_EXPIRED", "액세스 토큰 만료", "액세스 토큰 만료"), // B001
    REFRESH_TOKEN_EXPIRED("REFRESH_TOKEN_EXPIRED", "리프레쉬 토큰 만료", "로그인 시간이 만료되었습니다"), // B002
    WRONG_ACCOUNT("WRONG_ACCOUNT", "아이디/비밀번호 오류", "아이디 또는 비밀번호를 다시 확인해주세요."), // B003
    NO_ACCOUNT("NO_ACCOUNT", "계정 정보 없음", "계정 정보를 찾을 수 없어요"), // B004
    ALREADY_WATERED("ALREADY_WATERED", "이미 물 준 날짜", "이 날짜엔 이미 물을 줬어요"), // B005
    //////////////////// For ResourceNotFoundException
    NO_SUCH_CHEMICAL("NO_SUCH_CHEMICAL", "해당 약품 없음", "해당 약품을 찾을 수 없어요"),
    NO_SUCH_GOAL("NO_SUCH_GOAL", "해당 목표 없음", "해당 목표를 찾을 수 없어요"),
    NO_SUCH_PLACE("NO_SUCH_PLACE", "해당 장소 없음", "해당 장소를 찾을 수 없어요"),
    NO_SUCH_PLANT("NO_SUCH_PLANT", "해당 식물 없음", "해당 식물을 찾을 수 없어요"),
    NO_SUCH_ROUTINE("NO_SUCH_ROUTINE", "해당 루틴 없음", "해당 루틴을 찾을 수 없어요"),
    NO_SUCH_TODO("NO_SUCH_TODO", "해당 할 일 없음", "해당 할 일을 찾을 수 없어요"),
    NO_SUCH_WATERING("NO_SUCH_WATERING", "해당 물주기 기록 없음", "해당 물주기 기록을 찾을 수 없어요"),

    NO_ACCOUNT_FOR_EMAIL("NO_ACCOUNT_FOR_EMAIL", "제출 이메일에 해당하는 사용자 없음", "해당 이메일로 가입한 회원이 없어요"), // B007
    NO_TOKEN_IN_REDIS("NO_TOKEN_IN_REDIS", "레디스에 사용자 없음", "로그인 시간이 만료되었습니다. 다시 로그인해주세요"), // B009
    INVALID_JWT_TOKEN("INVALID_JWT_TOKEN", "유효하지 않은 액세스 토큰", "접근 권한이 없어요. 다시 로그인해주세요"), // B010
    INVALID_REFRESH_TOKEN("INVALID_REFRESH_TOKEN", "유효하지 않은 리프레쉬 토큰", "비정상적인 움직임이 발생했어요. 다시 로그인해주세요."), // B011
    CANNOT_LOGIN("CANNOT_LOGIN", "로그인 할 수 없음", "로그인 할 수 없음"), // B012
    //////////// FOR UnauthorizedAccessException
    NOT_YOUR_CHEMICAL("NOT_YOUR_CHEMICAL", "요청자의 약품이 아님", "당신의 약품이 아니에요"),
    NOT_YOUR_GOAL("NOT_YOUR_GOAL", "요청자의 목표가 아님", "당신의 목표가 아니에요"),
    NOT_YOUR_PLACE("NOT_YOUR_PLACE", "요청자의 장소가 아님", "당신의 장소가 아니에요"),
    NOT_YOUR_PLANT("NOT_YOUR_PLANT", "요청자의 식물이 아님", "당신의 식물이 아니에요"),
    NOT_YOUR_ROUTINE("NOT_YOUR_ROUTINE", "요청자의 루틴이 아님", "당신의 루틴이 아니에요"),
    NOT_YOUR_TODO("NOT_YOUR_TODO", "요청자의 할 일이 아님", "당신의 할 일이 아니에요"),
    NOT_YOUR_WATERING("NOT_YOUR_WATERING", "요청자의 물 주기 기록이 아님", "당신의 물 주기 기록이 아니에요"),

    INVALID_REQUEST_DATA("INVALID_REQUEST_DATA", "유효하지 않은 데이터", "입력 값을 확인해주세요"), // BE014
    NO_IDENTIFICATION_INFO_IN_REDIS("NO_IDENTIFICATION_INFO_IN_REDIS", "레디스에 해당 확인코드 정보 없음", "본인 확인 코드를 확인해주세요"), // BE015
    NOT_YOUR_IDENTIFICATION_CODE("NOT_YOUR_IDENTIFICATION_CODE", "본인 확인 코드 불일치", "본인 확인 코드를 확인해주세요"), // BE016
    UNEXPECTED_ERROR_OCCUR("UNEXPECTED_ERROR_OCCUR", "알 수 없는 오류 발생", "알 수 없는 오류 발생"),

    // 유효성 검사 관련
    INVALID_CHEMICAL_TYPE("INVALID_CHEMICAL_TYPE", "유효한 약품 타입이 아님", "약품 타입을 확인해주세요"),
    INVALID_PASSWORD("INVALID_PASSWORD", "비밀번호 유효성 검사 실패", "숫자, 특수문자를 포함하여 8자리 이상이어야 해요.");


    private static final Map<String, String> CODE_MAP = Collections.unmodifiableMap(
            Stream.of(values()).collect(Collectors.toMap(ExceptionCode::getCode, ExceptionCode::name))
    );

    private String code;
    private String description;
    private String message;

    ExceptionCode(String code, String description, String message) {
        this.code = code;
        this.description = description;
        this.message = message;
    }

    public static ExceptionCode of(String code) {
        return ExceptionCode.valueOf(CODE_MAP.get(code));
    }
}
