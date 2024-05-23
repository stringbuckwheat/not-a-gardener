package xyz.notagardener.common.error.exception;

import lombok.Getter;
import xyz.notagardener.common.error.code.ExceptionCode;

@Getter
public class VerificationException extends RuntimeException {
    private ExceptionCode code;

    public VerificationException(ExceptionCode code) {
        super(code.getDescription());
        this.code = code;
    }
}
