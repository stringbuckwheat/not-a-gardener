package com.buckwheat.garden.controller;

import com.buckwheat.garden.data.dto.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

// 여러 컨트롤러에 대해 전역적으로 ExceptionHandler 적용
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(BadCredentialsException.class)
    public HttpEntity<ErrorResponse> handleBadCredentialsException(BadCredentialsException e){
        log.debug("Exception Handler 호출");

        ErrorResponse er = new ErrorResponse();
        er.setCode(401);
        er.setError("아이디/비번 오류");
        er.setErrorDescription("아이디 또는 비밀번호를 다시 확인해주세요.");

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(er);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public HttpEntity<ErrorResponse> handleUsernameNotFoundException(UsernameNotFoundException e){
        log.debug("e.getMessage(): {}", e.getMessage());

        ErrorResponse er = ErrorResponse.builder()
                .code(401)
                .error("해당 계정 없음")
                .errorDescription(e.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(er);
    }

    @ExceptionHandler(IllegalStateException.class)
    public HttpEntity<ErrorResponse> handleIllegalStateException(IllegalStateException e){
        log.debug("e.getMessage(): {}", e.getMessage());

        ErrorResponse er = ErrorResponse.builder()
                .code(409)
                .error("이미 오늘 물을 줬어요")
                .errorDescription(e.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.CONFLICT).body(er);
    }
}
