package xyz.notagardener.authentication.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 로그인 요청
 */
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Login {
    @Schema(description = "아이디", example = "futurefarmer")
    @NotBlank(message = "아이디는 필수 입력값입니다.")
    private String username;

    @Schema(description = "비밀번호", example = "nowgardener")
    @NotBlank(message = "비밀번호는 필수 입력값입니다.")
    private String password;
}
