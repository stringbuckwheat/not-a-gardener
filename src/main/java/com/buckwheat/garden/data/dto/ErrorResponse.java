package com.buckwheat.garden.data.dto;

import lombok.Data;

@Data
public class ErrorResponse {
    private int code;
    private String message;
}
