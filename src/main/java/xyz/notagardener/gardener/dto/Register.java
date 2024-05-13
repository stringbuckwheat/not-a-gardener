package xyz.notagardener.domain.gardener.dto;

import xyz.notagardener.domain.gardener.Gardener;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
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
    @NotBlank
    private String username;

    @Email
    private String email;

    @NotBlank
    private String password;

    @NotBlank
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
