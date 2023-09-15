package com.buckwheat.garden.data.dto.gardener;

import com.buckwheat.garden.data.entity.Gardener;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 로그인 이후 토큰과 기본 정보 응답
 */
@AllArgsConstructor
@Getter // HttpMediaTypeNotAcceptableException
public class Info {
    private SimpleInfo simpleInfo;
    private Token token;

    public static Info from(String accessToken, String refreshToken, Gardener gardener) {
        return new Info(SimpleInfo.from(gardener), new Token(accessToken, refreshToken));
    }
}
