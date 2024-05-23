package xyz.notagardener.common.error.exception;

import lombok.Getter;
import xyz.notagardener.common.error.code.ExceptionCode;

@Getter
public class GardenerNotInSessionException extends RuntimeException{
    private ExceptionCode code;

    public GardenerNotInSessionException(ExceptionCode code) {
        super(code.getDescription());
        this.code = code;
    }
}
