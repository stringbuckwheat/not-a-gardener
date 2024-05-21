package xyz.notagardener.gardener.forgot;

import xyz.notagardener.authentication.dto.Login;

public interface ForgotService {
    // 본인 확인 코드 리턴 및 신원 확인
    Forgot forgotAccount(String email);

    // 본인 확인 코드 검증
    VerifyRequest verifyIdentificationCode(VerifyRequest verifyRequest);

    // 비밀번호 재설정에서 비밀번호 변경
    void resetPassword(Login login);
}
