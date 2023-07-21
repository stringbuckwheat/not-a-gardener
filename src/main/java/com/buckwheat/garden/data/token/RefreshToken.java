package com.buckwheat.garden.data.token;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class RefreshToken {
    private String token;
    private LocalDateTime expiredAt;

    // 객체 생성 금지
    private RefreshToken(){
        this.token = UUID.randomUUID().toString();
        this.expiredAt = LocalDateTime.now().plusHours(3);
    }

    public static RefreshToken getRefreshToken(){
        return new RefreshToken();
    }
}
