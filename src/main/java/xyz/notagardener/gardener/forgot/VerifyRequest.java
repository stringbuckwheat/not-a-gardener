package xyz.notagardener.gardener.forgot;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@AllArgsConstructor
public class VerifyRequest {
    private String email;
    private String identificationCode;
}
