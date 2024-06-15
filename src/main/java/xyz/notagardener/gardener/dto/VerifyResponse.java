package xyz.notagardener.gardener.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class VerifyResponse {
    @Schema(description = "무엇을 인증하는지", example = "본인 확인 코드")
    String subject; // 무엇을 인증하는지

    @Schema(description = "인증 여부", example = "true")
    Boolean verified;

    @Schema(description = "메시지", example = "본인 확인에 성공했어요")
    String messsage;

    public VerifyResponse(Boolean verified) {
        this.subject = "본인 확인 코드";
        this.verified = verified;
        this.messsage = "본인 확인에 성공했어요";
    }
}
