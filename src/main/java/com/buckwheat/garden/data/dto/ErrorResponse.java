package com.buckwheat.garden.data.dto;

import com.buckwheat.garden.error.ExceptionCode;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;


// @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorResponse {
    private String code;
    private String error;
    private String errorDescription;

    public static ErrorResponse from(ExceptionCode code){
        return new ErrorResponse(code.getCode(), code.getMessage(), code.getDescription());
    }
}
