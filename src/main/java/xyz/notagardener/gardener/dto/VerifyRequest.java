package xyz.notagardener.gardener.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@AllArgsConstructor
public class VerifyRequest {
    @NotBlank(message = "이메일은 비워둘 수 없어요.")
    @Email
    private String email;

    @NotBlank(message = "본인 확인 코드는 비워둘 수 없어요.")
    private String identificationCode;
}
