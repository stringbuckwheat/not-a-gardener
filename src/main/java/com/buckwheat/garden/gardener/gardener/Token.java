package com.buckwheat.garden.gardener.gardener;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Token {
    private String accessToken;
    private String refreshToken;
}