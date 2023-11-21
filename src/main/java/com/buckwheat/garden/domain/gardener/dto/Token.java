package com.buckwheat.garden.domain.gardener.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Token {
    private String accessToken;
    private String refreshToken;
}