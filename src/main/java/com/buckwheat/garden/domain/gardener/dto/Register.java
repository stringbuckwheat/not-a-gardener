package com.buckwheat.garden.domain.gardener.dto;

import com.buckwheat.garden.domain.gardener.Gardener;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 회원가입 요청
 */
@Getter
@Setter
@ToString
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class Register {
    private String username;
    private String email;
    private String password;
    private String name;

    /* 암호화된 password builder 패턴으로 사용중*/
    public Register encryptPassword(String BCryptpassword) {
        this.password = BCryptpassword;
        return this;
    }

    public Gardener toEntity() {
        return Gardener
                .builder()
                .username(username)
                .email(email)
                .password(password)
                .name(name)
                .createDate(LocalDateTime.now())
                .recentLogin(LocalDateTime.now())
                .build();
    }
}
