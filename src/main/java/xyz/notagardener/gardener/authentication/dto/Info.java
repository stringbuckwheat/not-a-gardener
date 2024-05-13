package xyz.notagardener.gardener.authentication.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import xyz.notagardener.gardener.Gardener;

import java.util.Objects;

/**
 * 로그인 이후 토큰과 기본 정보 응답
 */
@AllArgsConstructor
@Getter // HttpMediaTypeNotAcceptableException
@ToString
public class Info {
    @Schema(description = "(헤더용) 간단한 사용자 정보")
    private SimpleInfo simpleInfo;

    @Schema(description = "Access Token, Refresh Token")
    private Token token;

    public static Info from(String accessToken, String refreshToken, Gardener gardener) {
        return new Info(SimpleInfo.from(gardener), new Token(accessToken, refreshToken));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Info info = (Info) o;
        return simpleInfo.equals(info.simpleInfo) && token.equals(info.token);
    }

    @Override
    public int hashCode() {
        return Objects.hash(simpleInfo, token);
    }
}
