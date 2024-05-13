package xyz.notagardener.domain.gardener.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

/**
 * 아이디/비밀번호 찾기 응답
 */
@AllArgsConstructor
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
public class Forgot {
    @Schema(description = "본인 확인 코드", example = "ABC123")
    private String identificationCode;

    @Schema(description = "이메일", example = "testgardener@gardener.com")
    private String email;

    @Schema(description = "가입 계정들(중복 가입 허용)", example = "[gardener1, gardener2, gardener3]")
    List<Username> usernames;
}
