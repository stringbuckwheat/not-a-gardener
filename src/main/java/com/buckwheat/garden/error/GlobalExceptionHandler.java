package com.buckwheat.garden.error;

import com.buckwheat.garden.data.dto.ErrorResponse;
import com.buckwheat.garden.error.exception.AlreadyWateredException;
import com.buckwheat.garden.error.exception.ExpiredRefreshTokenException;
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
    /**
     * 리프레쉬 토큰 만료
     * @param e ExpiredRefreshTokenException(Custom)
     * @return
     */
    @ExceptionHandler(ExpiredRefreshTokenException.class)
    public HttpEntity<ErrorResponse> handleExpiredRefreshTokenException(ExpiredRefreshTokenException e){
        log.debug("ExpiredRefreshTokenException Handler 호출");

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ErrorResponse.from(ExceptionCode.REFRESH_TOKEN_EXPIRED));
    }

    /**
     * 로그인 시 ID/PW 틀림
     * @param e BadCredentialsException
     * @return
     */
    @ExceptionHandler(BadCredentialsException.class)
    public HttpEntity<ErrorResponse> handleBadCredentialsException(BadCredentialsException e){
        log.debug("BadCredentialsException Handler 호출");

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ErrorResponse.from(ExceptionCode.WRONG_ACCOUNT));
    }

    /**
     * 해당 계정 없음
     * @param e UsernameNotFoundException
     * @return
     */
    @ExceptionHandler(UsernameNotFoundException.class)
    public HttpEntity<ErrorResponse> handleUsernameNotFoundException(UsernameNotFoundException e){
        log.debug("UsernameNotFoundException Handler 호출");
        log.debug("e.getMessage(): {}", e.getMessage());

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ErrorResponse.from(ExceptionCode.NO_ACCOUNT));
    }

    /**
     * 이미 오늘 물 줬는데 또 물 주기 누름
     * @param e AlreadyWateredException(Custom)
     * @return
     */
    @ExceptionHandler(AlreadyWateredException.class)
    public HttpEntity<ErrorResponse> handleIllegalStateException(AlreadyWateredException e){
        log.debug("AlreadyWateredException Handler 호출");

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ErrorResponse.from(ExceptionCode.ALREADY_WATERED));
    }
}
