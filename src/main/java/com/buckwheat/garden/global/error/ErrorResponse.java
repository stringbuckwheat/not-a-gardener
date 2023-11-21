package com.buckwheat.garden.global.error;

import com.buckwheat.garden.global.error.code.ExceptionCode;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorResponse {
    private String code;
    private String title;
    private String message;

    public static ErrorResponse from(ExceptionCode code) {
        return new ErrorResponse(code.getCode(), code.getTitle(), code.getMessage());
    }
}
