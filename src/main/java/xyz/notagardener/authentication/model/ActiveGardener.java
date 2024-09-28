package xyz.notagardener.authentication.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import xyz.notagardener.authentication.token.RefreshToken;

import java.time.LocalDateTime;

@Getter
@RedisHash(value = "activeGardener", timeToLive = 10800)
@ToString
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ActiveGardener {
    @Id
    private Long gardenerId;
    private String name;
    private String provider;
    private RefreshToken refreshToken;
    private LocalDateTime createdAt;

    public void updateRefreshToken(RefreshToken newRefreshToken) {
        this.refreshToken = newRefreshToken;
    }
}
