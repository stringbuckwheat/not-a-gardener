package com.buckwheat.garden.data.dto.gardener;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Token {
    private String accessToken;
    private String refreshToken;
}