package xyz.notagardener.common.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import xyz.notagardener.common.error.code.ExceptionCode;
import xyz.notagardener.common.error.exception.*;

// 여러 컨트롤러에 대해 전역적으로 ExceptionHandler 적용
@RestControllerAdvice
@Slf4j
public class GardenExceptionHandler {

    @ExceptionHandler(UsernameNotFoundException.class)
    public HttpEntity<ErrorResponse> handleUsernameNotFoundException(UsernameNotFoundException e) {
        ExceptionCode exceptionCode = ExceptionCode.of(e.getMessage());

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse(exceptionCode));
    }

    @ExceptionHandler(HasSameUsernameException.class)
    public HttpEntity<ErrorResponse> handleHasSameUsernameException(HasSameUsernameException e) {
        ExceptionCode code = e.getCode();
        ErrorResponse errorResponse = new ErrorResponse(code.getCode(), code.getDescription(), e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    @ExceptionHandler(GardenerNotInSessionException.class)
    public HttpEntity<ErrorResponse> handleExpiredGardenerNotInSessionException(GardenerNotInSessionException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(e.getCode()));
    }

    @ExceptionHandler(ExpiredRefreshTokenException.class)
    public HttpEntity<ErrorResponse> handleExpiredRefreshTokenException(ExpiredRefreshTokenException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse(e.getCode()));
    }

    @ExceptionHandler(UnauthorizedAccessException.class)
    public HttpEntity<ErrorResponse> handleUnauthorizedAccessException(UnauthorizedAccessException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse(e.getCode()));
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public HttpEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(e.getCode()));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public HttpEntity<ErrorResponse> handleBadCredentialsException(BadCredentialsException e) {
        ExceptionCode exceptionCode = ExceptionCode.of(e.getMessage());

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorResponse(exceptionCode));
    }

    @ExceptionHandler(AlreadyWateredException.class)
    public HttpEntity<ErrorResponse> handleAlreadyWateredException(AlreadyWateredException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse(e.getCode()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public HttpEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException e) {
        log.debug("IllegalArgumentException: " + e.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(ExceptionCode.UNEXPECTED_ERROR_OCCUR));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public HttpEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        FieldError fieldError = e.getBindingResult().getFieldError();

        if (fieldError != null) {
            // @NotBlank 어노테이션의 직접 작성된 메시지 가져옴
            String errorMessage = fieldError.getDefaultMessage();
            String code = fieldError.getField().toUpperCase() + "_" + fieldError.getCode().toUpperCase();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(code, "", errorMessage));
        }

        // code, description, message
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("code", "description", "message"));
    }
}
