package xyz.notagardener.authentication.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
public class AuthTokens {
    @Schema(description = "액세스 토큰")
    private String accessToken;

    @Schema(description = "리프레쉬 토큰")
    private String refreshToken;
}