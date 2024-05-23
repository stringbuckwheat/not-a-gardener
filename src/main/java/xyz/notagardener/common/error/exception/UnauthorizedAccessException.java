package xyz.notagardener.common.error.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import xyz.notagardener.common.error.code.ExceptionCode;

/**
 * 본인 소유가 아닌 리소스에 대한 요청
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UnauthorizedAccessException extends RuntimeException {
    private ExceptionCode code;

    public UnauthorizedAccessException(ExceptionCode code) {
        super(code.getMessage());
        this.code = code;
    }
}
