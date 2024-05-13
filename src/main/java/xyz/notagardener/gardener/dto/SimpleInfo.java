package xyz.notagardener.domain.gardener.dto;

import xyz.notagardener.domain.gardener.Gardener;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class SimpleInfo {
    @Schema(description = "유저 PK", example = "1")
    private Long gardenerId;

    @Schema(description = "이름", example = "green")
    private String name;

    @Schema(description = "소셜 로그인 공급자", example = "")
    private String provider;

    public static SimpleInfo from(Gardener gardener) {
        return new SimpleInfo(gardener.getGardenerId(), gardener.getName(), gardener.getProvider());
    }

    public static SimpleInfo from(Long gardenerId, String name, String provider) {
        return new SimpleInfo(gardenerId, name, provider);
    }
}