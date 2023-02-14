package com.buckwheat.garden.controller;

import com.buckwheat.garden.data.dto.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

// 여러 컨트롤러에 대해 전역적으로 ExceptionHandler를 적용
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public HttpEntity<ErrorResponse> handleException(BadCredentialsException e){
        log.debug("Exception Handler 호출");

        ErrorResponse er = new ErrorResponse();
        er.setCode(401);
        er.setMessage("아이디 또는 비밀번호를 다시 확인해주세요.");

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(er);
    }
}
