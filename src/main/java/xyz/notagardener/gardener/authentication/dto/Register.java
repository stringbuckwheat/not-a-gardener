package xyz.notagardener.gardener.authentication.dto;

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
    @NotBlank
    private String username;

    @Email
    private String email;

    @NotBlank
    private String password;

    @NotBlank
    private String name;


    // 유효성 검사
    public boolean isValid() {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

        return username != null
                && username.matches("^[A-Za-z]{1}[A-Za-z0-9_-]{5,19}$") //
                && email != null
                && email.matches(emailRegex)
                && password != null
                && password.matches("(?=.*\\d{1,50})(?=.*[~`!@#$%\\^&*()-+=]{1,50})(?=.*[a-zA-Z]{2,50}).{8,50}")
                && name != null
                && name.length() > 0
                && name.length() < 20;
    }

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
