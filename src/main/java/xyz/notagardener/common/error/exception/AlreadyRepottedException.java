package xyz.notagardener.common.error.exception;

import lombok.Getter;
import xyz.notagardener.common.error.code.ExceptionCode;

@Getter
public class AlreadyRepottedException extends RuntimeException{
    private ExceptionCode code;

    public AlreadyRepottedException(ExceptionCode code) {
        super(code.getDescription());
        this.code = code;
    }
}
