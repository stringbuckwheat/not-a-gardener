package xyz.notagardener.gardener.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class VerifyResponse {
    String subject; // 무엇을 인증하는지
    Boolean verified;
    String messsage;

    public VerifyResponse(Boolean verified) {
        this.subject = "본인 확인 코드";
        this.verified = verified;
        this.messsage = "본인 확인에 성공했어요";
    }
}
