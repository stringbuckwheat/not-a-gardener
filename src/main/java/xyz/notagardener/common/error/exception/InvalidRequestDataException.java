package xyz.notagardener.common.error.exception;

import xyz.notagardener.common.error.code.ExceptionCode;

public class InvalidRequestDataException extends RuntimeException{
    private ExceptionCode code;

    public InvalidRequestDataException(ExceptionCode code) {
        super(code.getDescription());
        this.code = code;
    }
}
