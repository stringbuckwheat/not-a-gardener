package com.buckwheat.garden.service;

import com.buckwheat.garden.data.dto.GardenerDto;

public interface ForgotService {
    // 본인 확인 코드 리턴 및
    GardenerDto.Forgot forgotAccount(String email);

    // 본인 확인 메일 전송(아이디 찾기)
    void sendEmail(String identificationCode, String email);

    // 비밀번호 찾기에서 비밀번호 변경
    void resetPassword(GardenerDto.Login login);
}
