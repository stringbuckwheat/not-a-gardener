package com.buckwheat.garden.domain.gardener.dto;

import com.buckwheat.garden.domain.gardener.Gardener;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 로그인 이후 토큰과 기본 정보 응답
 */
@AllArgsConstructor
@Getter // HttpMediaTypeNotAcceptableException
public class Info {
    @Schema(description = "(헤더용) 간단한 사용자 정보")
    private SimpleInfo simpleInfo;

    @Schema(description = "Access Token, Refresh Token")
    private Token token;

    public static Info from(String accessToken, String refreshToken, Gardener gardener) {
        return new Info(SimpleInfo.from(gardener), new Token(accessToken, refreshToken));
    }
}
