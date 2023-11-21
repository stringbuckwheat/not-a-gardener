package com.buckwheat.garden.gardener;

import com.buckwheat.garden.gardener.gardener.Forgot;
import com.buckwheat.garden.gardener.gardener.Login;

public interface ForgotService {
    // 본인 확인 코드 리턴 및
    Forgot forgotAccount(String email);

    // 비밀번호 찾기에서 비밀번호 변경
    void resetPassword(Login login);
}
