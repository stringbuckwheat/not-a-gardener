package xyz.notagardener.common.error;

import xyz.notagardener.common.error.code.ExceptionCode;
import xyz.notagardener.common.error.exception.AlreadyWateredException;
import xyz.notagardener.common.error.exception.ExpiredRefreshTokenException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;

// 여러 컨트롤러에 대해 전역적으로 ExceptionHandler 적용
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    /**
     * 리프레쉬 토큰 만료
     *
     * @param e ExpiredRefreshTokenException(Custom)
     * @return
     */
    @ExceptionHandler(ExpiredRefreshTokenException.class)
    public HttpEntity<ErrorResponse> handleExpiredRefreshTokenException(ExpiredRefreshTokenException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ErrorResponse.from(ExceptionCode.REFRESH_TOKEN_EXPIRED));
    }

    @ExceptionHandler(NoSuchElementException.class)
    public HttpEntity<ErrorResponse> handleNoSuchElementException(NoSuchElementException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ErrorResponse.from(ExceptionCode.NO_SUCH_ITEM));
    }

    /**
     * 로그인 시 ID/PW 틀림
     *
     * @param e BadCredentialsException
     * @return
     */
    @ExceptionHandler(BadCredentialsException.class)
    public HttpEntity<ErrorResponse> handleBadCredentialsException(BadCredentialsException e) {
        ExceptionCode exceptionCode = ExceptionCode.of(e.getMessage());

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ErrorResponse.from(exceptionCode));
    }

    /**
     * 해당 계정 없음
     *
     * @param e UsernameNotFoundException
     * @return
     */
    @ExceptionHandler(UsernameNotFoundException.class)
    public HttpEntity<ErrorResponse> handleUsernameNotFoundException(UsernameNotFoundException e) {
        ExceptionCode exceptionCode = ExceptionCode.of(e.getMessage());

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ErrorResponse.from(exceptionCode));
    }

    /**
     * 이미 오늘 물 줬는데 또 물 주기 누름
     *
     * @param e AlreadyWateredException(Custom)
     * @return
     */
    @ExceptionHandler(AlreadyWateredException.class)
    public HttpEntity<ErrorResponse> handleAlreadyWateredException(AlreadyWateredException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ErrorResponse.from(ExceptionCode.ALREADY_WATERED));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public HttpEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(ErrorResponse.from(ExceptionCode.NOT_YOUR_THING));
    }
}
