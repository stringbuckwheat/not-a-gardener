package xyz.notagardener.common.error.code;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ExceptionCodeExample {
    PLEASE_LOGIN("인증이 필요한 엔드포인트", "{\"code\": \"B000\", \"title\": \"인증이 필요한 엔드포인트\", \"message\": \"로그인 해주세요\"}"),
    ACCESS_TOKEN_EXPIRED("액세스 토큰 만료", "{\"code\": \"B001\", \"title\": \"액세스 토큰 만료\", \"message\": \"액세스 토큰 만료\"}"),
    REFRESH_TOKEN_EXPIRED("리프레쉬 토큰 만료", "{\"code\": \"B002\", \"title\": \"리프레쉬 토큰 만료\", \"message\": \"로그인 시간이 만료되었습니다\"}"),
    WRONG_ACCOUNT("아이디/비밀번호 오류", "{\"code\": \"B003\", \"title\": \"아이디/비밀번호 오류\", \"message\": \"아이디 또는 비밀번호를 다시 확인해주세요.\"}"),
    NO_ACCOUNT("계정 정보 없음", "{\"code\": \"B004\", \"title\": \"계정 정보 없음\", \"message\": \"해당 유저를 찾을 수 없어요\"}"),
    ALREADY_WATERED("이미 물 준 날짜", "{\"code\": \"B005\", \"title\": \"이미 물 준 날짜\", \"message\": \"이 날짜엔 이미 물을 줬어요\"}"),
    NO_SUCH_ITEM("해당 아이템 없음", "{\"code\": \"B006\", \"title\": \"해당 아이템 없음\", \"message\": \"해당 아이템을 찾을 수 없어요\"}"),
    NO_ACCOUNT_FOR_EMAIL("해당 이메일의 가입 계정 없음", "{\"code\": \"B007\", \"title\": \"해당 이메일의 가입 계정 없음\", \"message\": \"해당 이메일로 가입한 회원이 없어요\"}"),
    WRONG_PASSWORD("비밀번호 오류", "{\"code\": \"B008\", \"title\": \"비밀번호 오류\", \"message\": \"비밀번호를 확인해주세요\"}"),
    NO_TOKEN_IN_REDIS("레디스에 사용자 없음", "{\"code\": \"B009\", \"title\": \"레디스에 사용자 없음\", \"message\": \"로그인 시간이 만료되었습니다. 다시 로그인해주세요\"}"),
    INVALID_REFRESH_TOKEN("유효하지 않은 리프레쉬 토큰", "{\"code\": \"B011\", \"title\": \"유효하지 않은 리프레쉬 토큰\", \"message\": \"비정상적인 움직임이 발생했어요. 다시 로그인해주세요.\"}"),
    INVALID_JWT_TOKEN("유효하지 않은 액세스 토큰", "{\"code\": \"B010\", \"title\": \"유효하지 않은 액세스 토큰\", \"message\": \"접근 권한이 없어요. 다시 로그인해주세요\"}"),
    CANNOT_LOGIN("로그인 할 수 없음", "{\"code\": \"B012\", \"title\": \"로그인 할 수 없음\", \"message\": \"로그인 할 수 없음\"}");

    public final String description;
    public final String jsonString;
}
