package xyz.notagardener.gardener.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import xyz.notagardener.gardener.dto.Username;

import java.time.LocalDateTime;
import java.util.List;

@RedisHash(value = "lostGardener", timeToLive = 300) // 5분
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class LostGardener {
    @Id
    private String email;
    private String identificationCode;
    private List<Username> usernames;
    private LocalDateTime createdAt;

    public LostGardener(String identificationCode, String email, List<Username> usernames) {
        this.identificationCode = identificationCode;
        this.email = email;
        this.usernames = usernames;
        this.createdAt = LocalDateTime.now();
    }
}
