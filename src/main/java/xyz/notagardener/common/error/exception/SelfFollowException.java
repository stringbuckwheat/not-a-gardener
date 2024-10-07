package xyz.notagardener.common.error.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import xyz.notagardener.common.error.code.ExceptionCode;

@AllArgsConstructor
@Getter
public class SelfFollowException extends RuntimeException {
    private ExceptionCode code;
}
