package com.buckwheat.garden.error.exception;

public class ExpiredRefreshTokenException extends RuntimeException{
    private String message;

    public ExpiredRefreshTokenException(){
        super();
        this.message = "로그인 시간이 만료되었습니다";
    }
}
