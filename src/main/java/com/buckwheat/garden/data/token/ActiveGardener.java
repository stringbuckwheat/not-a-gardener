package com.buckwheat.garden.data.token;

import com.buckwheat.garden.data.entity.Gardener;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.time.LocalDateTime;

@Getter
@RedisHash(value="activeGardeners")
@ToString
public class ActiveGardener {
    @Id
    private Long gardenerId;
    private RefreshToken refreshToken;
    private LocalDateTime createdAt;

    private ActiveGardener(Long gardenerId, RefreshToken refreshToken){
        this.gardenerId = gardenerId;
        this.refreshToken = refreshToken;
        this.createdAt = LocalDateTime.now();
    }

    public static ActiveGardener from(Gardener gardener, RefreshToken refreshToken){
        return new ActiveGardener(gardener.getGardenerId(), refreshToken);
    }
}
