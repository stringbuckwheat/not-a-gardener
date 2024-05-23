package xyz.notagardener.gardener.dto;

import jakarta.validation.constraints.Size;
import xyz.notagardener.common.validation.PasswordConstraints;
import xyz.notagardener.common.validation.UsernameConstraints;
import xyz.notagardener.gardener.Gardener;
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
    @NotBlank(message = "아이디는 비워둘 수 없어요")
    @UsernameConstraints
    private String username;

    @Email(message = "이메일 형식을 지켜주세요")
    private String email;

    @NotBlank(message = "비밀번호는 비워둘 수 없어요")
    @PasswordConstraints
    private String password;

    @NotBlank(message = "이름은 비워둘 수 없어요")
    @Size(min = 1, max = 30)
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
