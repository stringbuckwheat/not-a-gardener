package xyz.notagardener.authentication.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import xyz.notagardener.authentication.model.ActiveGardener;
import xyz.notagardener.gardener.Gardener;

@AllArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
public class Info {
    @Schema(description = "간단한 사용자 정보")
    private SimpleInfo info;

    @Schema(description = "Access Token, Refresh Token")
    private Token token;

    public Info(String accessToken, String refreshToken, Gardener gardener) {
        this.info = new SimpleInfo(gardener);
        this.token = new Token(accessToken, refreshToken);
    }

    public Info(ActiveGardener activeGardener) {
        this.info = new SimpleInfo(activeGardener);
        this.token = new Token("", activeGardener.getRefreshToken().getToken());
    }
}
