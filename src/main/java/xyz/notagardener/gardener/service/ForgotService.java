package xyz.notagardener.gardener.service;

import xyz.notagardener.authentication.dto.Login;
import xyz.notagardener.gardener.dto.Forgot;
import xyz.notagardener.gardener.dto.Username;
import xyz.notagardener.gardener.dto.VerifyRequest;
import xyz.notagardener.gardener.dto.VerifyResponse;

import java.util.List;

public interface ForgotService {
    // 본인 확인 코드 리턴 및 신원 확인
    Forgot forgotAccount(String email);

    // 아이디 찾기
    List<Username> getUsernameByEmail(String email);

    // 본인 확인 코드 검증
    VerifyResponse verifyIdentificationCode(VerifyRequest verifyRequest);

    // 비밀번호 재설정에서 비밀번호 변경
    void resetPassword(Login login);
}
