package xyz.notagardener.gardener.authentication.dto;

import lombok.ToString;
import xyz.notagardener.gardener.Gardener;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

@AllArgsConstructor
@Getter
@ToString
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SimpleInfo that = (SimpleInfo) o;
        return gardenerId.equals(that.gardenerId) && name.equals(that.name) && Objects.equals(provider, that.provider);
    }

    @Override
    public int hashCode() {
        return Objects.hash(gardenerId, name, provider);
    }
}