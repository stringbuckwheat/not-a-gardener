package com.buckwheat.garden.data.token;

import com.buckwheat.garden.data.entity.Gardener;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.time.LocalDateTime;

@Getter
@RedisHash(value="activeGardeners")
@ToString
@AllArgsConstructor
@Builder
public class ActiveGardener {
    @Id
    private Long gardenerId;
    private String name;
    private RefreshToken refreshToken;
    private LocalDateTime createdAt;

    public static ActiveGardener from(Gardener gardener, RefreshToken refreshToken){
        return ActiveGardener.builder()
                .gardenerId(gardener.getGardenerId())
                .name(gardener.getName())
                .refreshToken(refreshToken)
                .createdAt(LocalDateTime.now())
                .build();
    }

    public static ActiveGardener from(Long gardenerId, String name, RefreshToken refreshToken){
        return ActiveGardener.builder()
                .gardenerId(gardenerId)
                .name(name)
                .refreshToken(refreshToken)
                .createdAt(LocalDateTime.now())
                .build();
    }
}
