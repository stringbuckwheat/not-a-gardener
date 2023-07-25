package com.buckwheat.garden.error.code;

import lombok.Getter;

@Getter
public enum ExceptionCode {
    ACCESS_TOKEN_EXPIRED("B001", "액세스 토큰 만료", "액세스 토큰 만료"),
    REFRESH_TOKEN_EXPIRED("B002", "리프레쉬 토큰 만료", "로그인 시간이 만료되었습니다"),
    WRONG_ACCOUNT("B003", "아이디/비밀번호 오류", "아이디 또는 비밀번호를 다시 확인해주세요."),
    NO_ACCOUNT("B004", "계정 정보 없음", "해당 유저를 찾을 수 없어요"),
    ALREADY_WATERED("B005", "오늘 이미 물 줌", "이미 오늘 물을 줬어요"),
    NO_SUCH_ITEM("B006", "해당 아이템 없음", "해당 아이템을 찾을 수 없어요");

    private String code;
    private String title;
    private String message;

    ExceptionCode(String code, String title, String message){
        this.code = code;
        this.title = title;
        this.message = message;
    }
}
