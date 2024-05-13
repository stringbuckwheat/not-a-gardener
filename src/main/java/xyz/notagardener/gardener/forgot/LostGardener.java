package xyz.notagardener.gardener.forgot;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

import java.time.LocalDateTime;
import java.util.List;

@RedisHash(value = "lostGardener", timeToLive = 300) // 5분
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class LostGardener {
    @Id
    private String identificationCode;
    private String email;
    private List<Username> usernames;
    private LocalDateTime createdAt;

    public LostGardener(String identificationCode, String email, List<Username> usernames) {
        this.identificationCode = identificationCode;
        this.email = email;
        this.usernames = usernames;
        this.createdAt = LocalDateTime.now();
    }
}
