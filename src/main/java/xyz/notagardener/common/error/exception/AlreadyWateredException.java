package xyz.notagardener.common.error.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;
import xyz.notagardener.common.error.code.ExceptionCode;

@NoArgsConstructor
@Getter
public class AlreadyWateredException extends RuntimeException {
    private ExceptionCode code;

    public AlreadyWateredException(ExceptionCode code) {
        super(code.getDescription());
        this.code = code;
    }
}
