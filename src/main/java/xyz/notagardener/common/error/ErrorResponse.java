package xyz.notagardener.common.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import xyz.notagardener.common.error.code.ExceptionCode;


@Getter
@AllArgsConstructor
public class ErrorResponse {
    private String code;
    private String description;
    private String message;

    public ErrorResponse (ExceptionCode code) {
        this.code = code.getCode();
        this.description = code.getDescription();
        this.message = code.getMessage();
    }
}
