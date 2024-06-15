package xyz.notagardener.authentication.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import xyz.notagardener.authentication.model.ActiveGardener;
import xyz.notagardener.gardener.model.Gardener;

@AllArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
public class SimpleInfo {
    @Schema(description = "유저 PK", example = "1")
    private Long gardenerId;

    @Schema(description = "이름", example = "green")
    private String name;

    @Schema(description = "소셜 로그인 공급자", example = "")
    private String provider;

    SimpleInfo(ActiveGardener activeGardener) {
        this.gardenerId = activeGardener.getGardenerId();
        this.name = activeGardener.getName();
        this.provider = activeGardener.getProvider();
    }

    SimpleInfo(Gardener gardener) {
        this.gardenerId = gardener.getGardenerId();
        this.name = gardener.getName();
        this.provider = gardener.getProvider();
    }
}